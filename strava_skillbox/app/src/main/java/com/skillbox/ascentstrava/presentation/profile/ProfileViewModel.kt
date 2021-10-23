package com.skillbox.ascentstrava.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import com.skillbox.ascentstrava.presentation.profile.data.ProfileRepository
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import javax.inject.Inject
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val athleteManager: AthleteManager,
    private val authManager: AuthManager
) : ViewModel() {

    private val athleteLiveData = MutableLiveData<Athlete>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val clearDataLiveData = MutableLiveData<Boolean>()

    val athlete: LiveData<Athlete>
        get() = athleteLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    val clearData: LiveData<Boolean>
        get() = clearDataLiveData

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

    fun getProfileUrl(): String? {
        return athleteManager.getProfileUrl()
    }

    fun putAthlete(athlete: Athlete) {
        athleteManager.putAthlete(athlete)
    }

    fun getAthleteFromCache(): Athlete? {
        return athleteManager.getAthlete()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                profileRepository.logout()
                clearDataLiveData.postValue(true)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

    fun clearData() {
        authManager.logout()
        athleteManager.clearAthlete()
        //todo очистить базу данных
    }
}