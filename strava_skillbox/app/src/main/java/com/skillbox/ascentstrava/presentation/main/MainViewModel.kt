package com.skillbox.ascentstrava.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.main.data.MainRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val isFirstTimeEntry = MutableLiveData<Boolean>()

    val isFirstEntry: LiveData<Boolean>
        get() = isFirstTimeEntry

    fun addFlagAfterFirstEntry() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addFlagAfterFirstEntry()
        }
    }

    fun isFirstEntry() {
        if (!mainRepository.isFirstEntry()) {
            isFirstTimeEntry.postValue(false)
        } else {
            isFirstTimeEntry.postValue(true)
        }
    }
}