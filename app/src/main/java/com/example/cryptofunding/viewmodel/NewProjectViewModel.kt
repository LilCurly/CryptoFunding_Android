package com.example.cryptofunding.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project

class NewProjectViewModel: ViewModel() {
    var projectName: MutableLiveData<String> = MediatorLiveData<String>()
    var projectSummary: MutableLiveData<String> = MediatorLiveData<String>()
    var canProceed: MutableLiveData<Boolean> = MediatorLiveData<Boolean>()
    val projectImages: MutableList<Image> by lazy {
        mutableListOf<Image>()
    }
    lateinit var categoryName: String

    fun updateCanProceed() {
        if (!projectName.value.isNullOrEmpty() && !projectSummary.value.isNullOrEmpty() && projectImages.size > 0) {
            canProceed.postValue(true)
        }
        else {
            canProceed.postValue(false)
        }
    }

    fun getProject(): Project {
        return Project(
            projectName.value.toString(), projectSummary.value.toString(), CategoryType.valueOf(
                categoryName
            ), projectImages
        )
    }
}