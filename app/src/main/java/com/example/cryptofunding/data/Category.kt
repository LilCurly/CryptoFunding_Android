package com.example.cryptofunding.data

import androidx.annotation.IdRes

data class Category(val type: CategoryType, val img: Int)

enum class CategoryType(val title: String) {
    Brain("Technologie"),
    Blacksmith("Artisanat"),
    Blockchain("Film"),
    Drug("Sciences"),
    Garbage("Littérature"),
    Camera("Art"),
    Joystick("Jeux")
}