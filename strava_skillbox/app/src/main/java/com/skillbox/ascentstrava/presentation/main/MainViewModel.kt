package com.skillbox.ascentstrava.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.main.data.MainRepository
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val athleteManager: AthleteManager
) : ViewModel() {

    private val isFirstTimeEntry = MutableLiveData(mainRepository.isFirstEntry())

    val isFirstEntry: LiveData<Boolean>
        get() = isFirstTimeEntry

    fun addFlagAfterFirstEntry() {
        mainRepository.addFlagAfterFirstEntry()
    }

    fun containsAccessToken(): Boolean {
        return mainRepository.containsAccessToken()
    }

    fun getProfileUrl(): String? {
        return athleteManager.getProfileUrl()
    }
}