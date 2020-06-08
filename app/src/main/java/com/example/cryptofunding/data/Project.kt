package com.example.cryptofunding.data

import androidx.annotation.IdRes

data class Project(val name: String, val category: CategoryType, val percentFunded: Int, val img: Int, var isFavorite: Boolean = false)