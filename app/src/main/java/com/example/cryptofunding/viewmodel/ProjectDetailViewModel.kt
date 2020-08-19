package com.example.cryptofunding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.data.repository.ProjectRepository
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {
    val tasks: MediatorLiveData<List<Task>> by lazy {
        MediatorLiveData<List<Task>>()
    }
    lateinit var project: Project

    fun loadTasksForId(projectId: String) {
        projectRepository.loadTasksForId(projectId) {
            tasks.value = it
        }
    }

    fun setFavorite() {
        projectRepository.setFavorite(project.id!!)
    }

    fun removeFavorite() {
        projectRepository.removeFavorite(project.id!!)
    }
}