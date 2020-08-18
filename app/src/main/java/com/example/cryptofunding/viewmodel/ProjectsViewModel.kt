package com.example.cryptofunding.viewmodel

import androidx.lifecycle.*
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.repository.ProjectRepository
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(private val projectRepository: ProjectRepository) : ViewModel() {
    val currentCategory = MutableLiveData<Category?>()
    private val projects: MediatorLiveData<List<Project>> = MediatorLiveData()

    fun setCurrentCategory(category: Category?) {
        currentCategory.value = category
    }

    fun toggleFavorite(position: Int) {
        projects.value?.let {
            it[position].isFavorite = !it[position].isFavorite
        }
    }

    fun isFavorite(position: Int): Boolean {
        projects.value?.let {
            return it[position].isFavorite
        }
        return false
    }

    fun getProjects(): LiveData<List<Project>> {
        projectRepository.getAllTrendingProjects {
            projects.value = it
        }
        return projects
    }

    fun setFavorite(projectId: String) {
        projectRepository.setFavorite(projectId)
    }

    fun removeFavorite(projectId: String) {
        projectRepository.removeFavorite(projectId)
    }

    fun getCategories(): List<Category> {
        return ProjectRepository.categories
    }
}