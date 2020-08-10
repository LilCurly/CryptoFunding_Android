package com.example.cryptofunding.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.Task
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(): ViewModel() {
    var taskName: MutableLiveData<String> = MediatorLiveData<String>()
    var taskSummary: MutableLiveData<String> = MediatorLiveData<String>()
    var taskAmount: MutableLiveData<String> = MediatorLiveData<String>()
    var taskFinalDate: MutableLiveData<String> = MediatorLiveData<String>()

    var canProceed: MutableLiveData<Boolean> = MediatorLiveData<Boolean>()

    fun updateCanProceed() {
        if (!taskName.value.isNullOrEmpty() &&
            !taskSummary.value.isNullOrEmpty() &&
            !taskAmount.value.isNullOrEmpty() &&
            !taskFinalDate.value.isNullOrEmpty()) {
            canProceed.postValue(true)
        }
        else {
            canProceed.postValue(false)
        }
    }

    fun getTask(): Task {
        return Task(taskName.value.toString(),
            taskSummary.value.toString(),
            taskAmount.value?.toInt() ?: 0,
            taskFinalDate.value.toString())
    }
}