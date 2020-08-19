package com.example.cryptofunding.data

data class Task constructor(val title: String, val summary: String, val amount: Double, val limitDate: String, var expanded: Boolean = false) {
}