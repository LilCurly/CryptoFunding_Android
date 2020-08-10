package com.example.cryptofunding.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.DEBUG
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
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

        val projects = listOf(
            Project("Test", CategoryType.Camera, 46, R.drawable.avengers_poster),
            Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
            Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster, true),
            Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
            Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
            Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
            Project("Test2", CategoryType.Camera, 46, R.drawable.avengers_poster)
        )
    }

    fun saveProject(project: Project) {
        project.tempImages?.let {
            storeImages(it, project)
        }
    }

    private fun storeProject(
        project: Project,
        imagesUri: MutableList<String>
    ) {
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
                    tasksRef.add(taskData)
                }
            }
            .addOnFailureListener {
                Log.d(DEBUG, "Firestore error: $it")
            }
    }

    private fun storeImages(
        images: List<Image>,
        project: Project
    ) {
        val imagesUri = mutableListOf<String>()
        images.forEach { image ->
            val file = Uri.fromFile(File(image.path))
            val storageRef = storage.child("images")

            storageRef.child(file.toFile().name).putFile(file).addOnSuccessListener {
                val downloadUrl = it.storage.downloadUrl
                downloadUrl.addOnSuccessListener { url ->
                    imagesUri.add(url.toString())
                    storeProject(project, imagesUri)
                }.addOnFailureListener { error ->
                    Log.d(DEBUG, "Storage error: $error")
                }
            }.addOnFailureListener {
                Log.d(DEBUG, "Storage error: $it")
            }
        }
    }
}