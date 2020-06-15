package com.example.cryptofunding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.data.repository.ProjectRepository
import javax.inject.Inject

class DetailedProjectListViewModel @Inject constructor(): ViewModel() {
    val currentCategory = MutableLiveData<Category?>()

    fun setCurrentCategory(category: Category?) {
        currentCategory.value = category
    }

    fun toggleFavorite(position: Int) {
        ProjectRepository.projects[position].isFavorite = !ProjectRepository.projects[position].isFavorite
    }

    fun isFavorite(position: Int): Boolean {
        return ProjectRepository.projects[position].isFavorite
    }

    fun getProjects(): List<Project> {
        return ProjectRepository.projects
    }

    fun getCategories(): List<Category> {
        return ProjectRepository.categories
    }
}