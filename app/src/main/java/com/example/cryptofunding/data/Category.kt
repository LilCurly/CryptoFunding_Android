package com.example.cryptofunding.data

import androidx.annotation.IdRes

data class Category(val type: CategoryType, val img: Int)

enum class CategoryType(val title: String) {
    Brain("Brain"),
    Blacksmith("Blacksmith"),
    Blockchain("Blockchain"),
    Drug("Drug"),
    Garbage("Garbage"),
    Camera("Camera"),
    Joystick("Joystick")
}