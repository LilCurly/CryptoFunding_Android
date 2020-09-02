package com.example.cryptofunding.data

import androidx.annotation.IdRes

data class Category(val type: CategoryType, val img: Int)

enum class CategoryType(val title: String) {
    Technologie("Technologie"),
    Artisanat("Artisanat"),
    Film("Film"),
    Sciences("Sciences"),
    Litterature("Litterature"),
    Art("Art"),
    Jeux("Jeux")
}