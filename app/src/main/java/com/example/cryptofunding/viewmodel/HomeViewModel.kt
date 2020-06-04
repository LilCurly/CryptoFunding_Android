package com.example.cryptofunding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project
import javax.inject.Inject

class HomeViewModel @Inject constructor(): ViewModel() {
    val categories = listOf(
        Category(CategoryType.Blacksmith, R.drawable.blacksmith),
        Category(CategoryType.Blockchain, R.drawable.blockchain),
        Category(CategoryType.Brain, R.drawable.brain),
        Category(CategoryType.Drug, R.drawable.drug),
        Category(CategoryType.Garbage, R.drawable.garbage),
        Category(CategoryType.Camera, R.drawable.camera),
        Category(CategoryType.Joystick, R.drawable.joystick))
    val currentCategory = MutableLiveData<Category?>()

    val projects = listOf(
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster),
        Project("The Avengers | New movies in 4K", CategoryType.Camera, 46, R.drawable.avengers_poster)
    )

    fun setCurrentCategory(category: Category?) {
        currentCategory.value = category
    }
}