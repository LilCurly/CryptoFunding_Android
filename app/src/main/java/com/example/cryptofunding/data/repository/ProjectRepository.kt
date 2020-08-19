package com.example.cryptofunding.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.*
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.data.mapper.ProjectMapper
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.LoggedWallet
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Tasks.await
import com.google.android.gms.tasks.Tasks.whenAll
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class ProjectRepository @Inject constructor(private val firestore: FirebaseFirestore,
                                            private val storage: StorageReference) {
    companion object {
        val categories = listOf(
            Category(CategoryType.Blacksmith, R.drawable.blacksmith),
            Category(CategoryType.Blockchain, R.drawable.blockchain),
            Category(CategoryType.Brain, R.drawable.brain),
            Category(CategoryType.Drug, R.drawable.drug),
            Category(CategoryType.Garbage, R.drawable.garbage),
            Category(CategoryType.Camera, R.drawable.camera),
            Category(CategoryType.Joystick, R.drawable.joystick)
        )
    }

    fun getAllTrendingProjects(onComplete: (projectsList: List<Project>) -> Unit) {
        firestore.collection("projects").get().addOnCompleteListener { projectsTask ->
            projectsTask.result?.let { projectsQuery ->
                firestore.collection("favorites")
                    .document(LoggedWallet.currentlyLoggedWallet?.publicKey ?: "0")
                    .get()
                    .addOnCompleteListener { favTask ->
                        favTask.result?.let { favDoc ->
                            val projectsList = mutableListOf<Project>()
                            if (favDoc.exists()) {
                                projectsQuery.documents.forEach { doc ->
                                    projectsList.add(ProjectMapper.mapToProjectWithFavorites(doc, favDoc))
                                }
                            }
                            else {
                                projectsQuery.documents.forEach { doc ->
                                    projectsList.add(ProjectMapper.mapToProject(doc))
                                }
                            }
                            onComplete(projectsList)
                        }
                    }
            }
        }
    }

    fun getFavoritesProjects(onComplete: (projectsList: List<Project>) -> Unit) {
        LoggedWallet.currentlyLoggedWallet?.let { wallet ->
            firestore.collection("favorites").document(wallet.publicKey).get()
                .addOnCompleteListener { favTask ->
                    favTask.result?.let { favDoc ->
                        if (favDoc.exists()) {
                            val links = favDoc.get("links") as List<String>
                            if (links.isNotEmpty()) {
                                firestore.collection("projects").whereIn(FieldPath.documentId(), links).get()
                                    .addOnCompleteListener {
                                        it.result?.let { projectsQuery ->
                                            val projectsList = mutableListOf<Project>()
                                            projectsQuery.documents.forEach { projectDoc ->
                                                val project = ProjectMapper.mapToProject(projectDoc)
                                                project.isFavorite = true
                                                projectsList.add(project)
                                            }
                                            onComplete(projectsList)
                                        }
                                    }
                            }
                            onComplete(listOf())
                        }
                    }
                }
        }
    }

    suspend fun saveProject(project: Project, onSuccess: () -> Unit, onFailure: () -> Unit) {
        project.tempImages?.let {
            val imagesUrl = mutableListOf<String>()
            storeImages(it, imagesUrl, onSuccess, onFailure, project)
        }
    }

    private fun storeProject(project: Project, imagesUri: MutableList<String>, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val asyncTaskList = mutableListOf<Task<DocumentReference>>()
        val ref = firestore.collection("projects")

        val projectData = HashMap<String, Any>()
        projectData["title"] = project.name
        projectData["summary"] = project.summary
        projectData["images"] = imagesUri
        projectData["category"] = project.category.title

        ref.add(projectData)
            .addOnSuccessListener {
                val tasksRef = firestore.collection("projects")
                    .document(it.id)
                    .collection("tasks")

                project.tasks.forEach { task ->
                    val taskData = HashMap<String, Any>()
                    taskData["title"] = task.title
                    taskData["summary"] = task.summary
                    taskData["amount"] = task.amount
                    taskData["limitDate"] = task.limitDate
                    asyncTaskList.add(tasksRef.add(taskData))
                }
            }
            .addOnFailureListener {
                onFailure()
            }

        whenAll(asyncTaskList).addOnSuccessListener {
            onSuccess()
        }
    }

    private suspend fun storeImages(images: List<Image>, imagesUrl: MutableList<String>, onSuccess: () -> Unit, onFailure: () -> Unit, project: Project) {
        val asyncTasksList: MutableList<UploadTask> = mutableListOf()
        val asyncUrlList = mutableListOf<Task<Uri>>()
        coroutineScope {
            images.forEach { image ->
                val file = Uri.fromFile(File(image.path))
                val storageRef = storage.child("images")

                launch(Dispatchers.IO) {
                    val asyncTask = storageRef.child(file.toFile().name).putFile(file)
                    asyncTasksList.add(asyncTask)
                    asyncTask.addOnSuccessListener {
                        val downloadUrl = it.storage.downloadUrl
                        asyncUrlList.add(downloadUrl)
                        downloadUrl.addOnSuccessListener { url ->
                            imagesUrl.add(url.toString())
                        }
                    }
                }
            }
        }

        whenAll(asyncTasksList).addOnSuccessListener {
            whenAll(asyncUrlList).addOnSuccessListener {
                storeProject(project, imagesUrl, onSuccess, onFailure)
            }
        }
    }

    fun setFavorite(projectId: String) {
        LoggedWallet.currentlyLoggedWallet?.let { wallet ->
            firestore.collection("favorites").document(wallet.publicKey).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        var links = mutableListOf<String>()
                        if (it.exists()) {
                            links = it.get("links") as MutableList<String>
                        }
                        links.add(projectId)
                        firestore.collection("favorites").document(wallet.publicKey).set(mapOf(
                            "links" to links
                        ))
                    }
                }
            }
        }
    }

    fun removeFavorite(projectId: String) {
        LoggedWallet.currentlyLoggedWallet?.let { wallet ->
            firestore.collection("favorites").document(wallet.publicKey).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        if (it.exists()) {
                            val links = it.get("links") as MutableList<String>
                            links.remove(projectId)
                            firestore.collection("favorites").document(wallet.publicKey).set(mapOf(
                                "links" to links
                            ))
                        }
                    }
                }
            }
        }
    }
}