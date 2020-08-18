package com.example.cryptofunding.viewmodel

import androidx.lifecycle.*
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.repository.ProjectRepository
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(val projectRepository: ProjectRepository) : ViewModel() {
    val currentCategory = MutableLiveData<Category?>()
    private val projects: MediatorLiveData<List<Project>> = MediatorLiveData()

    fun setCurrentCategory(category: Category?) {
        currentCategory.value = category
    }

    fun toggleFavorite(position: Int) {
        ProjectRepository.projects[position].isFavorite = !ProjectRepository.projects[position].isFavorite
    }

    fun isFavorite(position: Int): Boolean {
        return ProjectRepository.projects[position].isFavorite
    }

    fun getProjects(): LiveData<List<Project>> {
        projectRepository.getAllTrendingProjects {
            projects.value = it
        }
        return projects
    }

    fun getCategories(): List<Category> {
        return ProjectRepository.categories
    }
}