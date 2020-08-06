package com.example.cryptofunding.data

import com.esafirm.imagepicker.model.Image

class Project {

    constructor()

    constructor(name: String, categoryType: CategoryType, percentFunded: Int, img: Int, isFavorite: Boolean = false) {
        this.name = name
        this.category = categoryType
        this.img = img
        this.isFavorite = isFavorite
        this.percentFunded = percentFunded
    }

    lateinit var name: String
    lateinit var summary: String
    lateinit var category: CategoryType
    var isFavorite: Boolean = false
    var percentFunded: Int = 0
    var img: Int = 0
    var tempImages: List<Image>? = null
}