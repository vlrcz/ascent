package com.skillbox.ascentstrava.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.profile.data.ProfileRepository
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import javax.inject.Inject
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val athleteLiveData = MutableLiveData<Athlete>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val athlete: LiveData<Athlete>
        get() = athleteLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun getProfileInfo() {
        viewModelScope.launch {
            try {
                val athlete = profileRepository.getProfileInfo()
                athleteLiveData.postValue(athlete)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

    fun changeAthleteWeight(weight: UpdateRequestBody) {
        viewModelScope.launch {
            try {
                profileRepository.changeAthleteWeight(weight)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}