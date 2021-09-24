package com.skillbox.ascentstrava.storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class StorageViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val isFirstTimeEntry = MutableLiveData<Boolean>()

    val isFirstEntry: LiveData<Boolean>
        get() = isFirstTimeEntry
}