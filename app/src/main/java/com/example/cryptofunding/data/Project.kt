package com.example.cryptofunding.data

import android.os.Parcelable
import com.esafirm.imagepicker.model.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
class Project(var name: String, var summary: String, var category: CategoryType, var tempImages: List<Image>? = null): Parcelable {



    constructor(name: String, categoryType: CategoryType, percentFunded: Int, img: Int, isFavorite: Boolean = false) : this(name, "", categoryType) {
        this.name = name
        this.category = categoryType
        this.img = img
        this.isFavorite = isFavorite
        this.percentFunded = percentFunded
    }

    var totalAmount: Int = 0
    var id: String? = null
    var isFavorite: Boolean = false
    var percentFunded: Int = 0
    var img: Int = 0
    var tasks = listOf<Task>()
    var imagesUrl = listOf<String>()
}