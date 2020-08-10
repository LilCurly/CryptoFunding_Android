package com.example.cryptofunding.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.data.repository.ProjectRepository
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

    fun saveProject() {
        setProjectTasks()
        projectRepository.saveProject(project)
    }
}