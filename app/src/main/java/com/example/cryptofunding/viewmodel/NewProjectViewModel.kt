package com.example.cryptofunding.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.data.CategoryType
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.DEBUG

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
        val project = Project()
        project.name = projectName.value.toString()
        project.summary = projectSummary.value.toString()
        project.tempImages = projectImages
        project.category = CategoryType.valueOf(categoryName)
        return project
    }
}