package com.skillbox.ascentstrava.presentation.athlete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.AuthRepository
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteManager
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteRepository
import com.skillbox.ascentstrava.presentation.athlete.data.UpdateRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlinx.coroutines.launch

class AthleteViewModel @Inject constructor(
    private val athleteRepository: AthleteRepository,
    private val athleteManager: AthleteManager,
    private val authManager: AuthManager,
    private val connectionManager: ConnectionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val athleteLiveData = MutableLiveData<Athlete>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val clearLiveData = MutableLiveData<Boolean>()
    private val networkLiveData = MutableLiveData<Boolean>()

    val athlete: LiveData<Athlete>
        get() = athleteLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    val clearData: LiveData<Boolean>
        get() = clearLiveData

    val isNetworkAvailable: LiveData<Boolean>
        get() = networkLiveData

    init {
        viewModelScope.launch {
            athleteManager
                .observeAthlete()
                .collect {
                    if (it != null) {
                        athleteLiveData.postValue(it)
                    }
                }
        }

        viewModelScope.launch {
            connectionManager
                .observeNetworkState()
                .collect {
                    networkLiveData.postValue(it)
                }
        }
    }

    fun changeAthleteWeight(weight: UpdateRequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                athleteRepository.changeAthleteWeight(weight)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

    fun getProfileUrl(): String? {
        return athleteManager.getProfileUrl()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.logout()
                authManager.logout()
                clearLiveData.postValue(true)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}