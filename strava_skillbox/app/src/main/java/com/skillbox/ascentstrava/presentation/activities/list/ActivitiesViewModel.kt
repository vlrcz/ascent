package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityModel>>()
    private val listFromDbLiveEvent = SingleLiveEvent<Boolean>()
    private val errorLiveEvent = SingleLiveEvent<String>()

    val activitiesLiveData: LiveData<List<ActivityModel>>
        get() = activitiesMutableLiveData

    val listFromDbLiveData: LiveData<Boolean>
        get() = listFromDbLiveEvent

    val errorLiveData: LiveData<String>
        get() = errorLiveEvent


    fun loadList() {
        viewModelScope.launch {
            try {
                val listFromDb = activitiesRepository.getActivitiesFromDb()
                activitiesMutableLiveData.postValue(listFromDb)
                listFromDbLiveEvent.postValue(true)
               /* val activitiesList = activitiesRepository.getActivities()
                activitiesMutableLiveData.postValue(activitiesList)*/
            } catch (t: Throwable) {

            }
        }
    }

    fun getAthlete(): Athlete? {
        return athleteManager.getAthlete()
    }
}