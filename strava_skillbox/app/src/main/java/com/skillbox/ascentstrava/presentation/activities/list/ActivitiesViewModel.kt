package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityModel>>()

    val activitiesLiveData: LiveData<List<ActivityModel>>
        get() = activitiesMutableLiveData

    fun loadList() {
        viewModelScope.launch {
            try {
                val activitiesList = activitiesRepository.getActivities()
                activitiesMutableLiveData.postValue(activitiesList)
            } catch (t: Throwable) {
                activitiesMutableLiveData.postValue(emptyList())
            }
        }
    }

    fun getAthlete(): Athlete? {
        return athleteManager.getAthlete()
    }
}