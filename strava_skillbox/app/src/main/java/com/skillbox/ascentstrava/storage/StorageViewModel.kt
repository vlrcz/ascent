package com.skillbox.ascentstrava.storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.di.StorageRepository

class StorageViewModel(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val isFirstTimeEntry = MutableLiveData<Boolean>()

    val isFirstEntry: LiveData<Boolean>
        get() = isFirstTimeEntry
}