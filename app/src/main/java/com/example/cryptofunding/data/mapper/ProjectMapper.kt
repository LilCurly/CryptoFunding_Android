package com.example.cryptofunding.data.mapper

import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project
import com.google.firebase.firestore.DocumentSnapshot

class ProjectMapper {
    companion object {
        fun mapToProject(document: DocumentSnapshot): Project {
            val title = document.getString("title")!!
            val summary = document.getString("summary")!!
            val category = document.getString("category")!!
            val images = document.get("images") as List<String>
            val categoryType = CategoryType.valueOf(category)

            val project = Project(title, summary, categoryType)
            project.imagesUrl = images
            project.id = document.id

            return project
        }
    }
}