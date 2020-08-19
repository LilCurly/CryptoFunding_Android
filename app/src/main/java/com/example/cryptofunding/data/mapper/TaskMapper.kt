package com.example.cryptofunding.data.mapper

import com.example.cryptofunding.data.Task
import com.google.firebase.firestore.DocumentSnapshot

class TaskMapper {
    companion object {
        fun mapToTask(document: DocumentSnapshot): Task {
            val title = document.getString("title")!!
            val summary = document.getString("summary")!!
            val limitDate = document.getString("limitDate")!!
            val amount = document.getLong("amount")!!.toInt()

            return Task(title, summary, amount, limitDate)
        }
    }
}