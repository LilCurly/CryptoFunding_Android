package com.example.cryptofunding.viewmodel

import androidx.lifecycle.*
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.data.repository.ProjectRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class HandleTasksViewModel @Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {
    val canProceed: MutableLiveData<Boolean> = MediatorLiveData<Boolean>()
    val tasks: MutableList<Task> by lazy {
        mutableListOf<Task>()
    }
    lateinit var project: Project

    fun updateCanProceed() {
        if (tasks.isNullOrEmpty()) {
            canProceed.postValue(false)
        }
        else {
            canProceed.postValue(true)
        }
    }

    private fun setProjectTasks() {
        project.tasks = tasks.toList()
    }

    fun saveProject(onSuccess: () -> Unit, onFailure: () -> Unit, onWaiting: () -> Unit) {
        onWaiting()
        setProjectTasks()
        runBlocking {
            projectRepository.saveProject(project, onSuccess, onFailure)
        }
    }
}