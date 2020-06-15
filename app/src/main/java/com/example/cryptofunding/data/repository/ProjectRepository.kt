package com.example.cryptofunding.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project

class ProjectRepository {
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
}