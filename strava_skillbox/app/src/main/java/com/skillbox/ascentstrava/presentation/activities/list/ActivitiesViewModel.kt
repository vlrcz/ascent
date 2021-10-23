package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityItem
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityItem>>()
    private val errorLiveEvent = SingleLiveEvent<String>()
    private val sentPendingActivitiesLiveEvent = SingleLiveEvent<Unit>()
    private val activitiesFromDbLiveEvent = SingleLiveEvent<Unit>()
    private val pendingActivitiesMutableLiveData = MutableLiveData<List<ActivityEntity>>()

    val activitiesLiveData: LiveData<List<ActivityItem>>
        get() = activitiesMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorLiveEvent

    val activitiesFromDbLiveData: LiveData<Unit>
        get() = activitiesFromDbLiveEvent

    val sentPendingActivitiesLiveData: LiveData<Unit>
        get() = sentPendingActivitiesLiveEvent

    val pendingActivitiesLiveData: LiveData<List<ActivityEntity>>
        get() = pendingActivitiesMutableLiveData

    fun loadList() {
        viewModelScope.launch {
            try {
                val activitiesList = activitiesRepository.getActivities()
                activitiesMutableLiveData.postValue(activitiesList)
            } catch (t: Throwable) {
                if (t is UnknownHostException) {
                    activitiesFromDbLiveEvent.postValue(Unit)
                } else {
                    errorLiveEvent.postValue(t.message)
                }
            }
        }
    }

    fun loadListFromDb() {
        viewModelScope.launch {
            try {
                val listFromDb = activitiesRepository.getActivitiesFromDb()
                val list = listFromDb.map {
                    ActivityItem(
                        uniqueId = it.id,
                        stravaId = null,
                        activityName = it.activityName,
                        activityType = it.activityType,
                        startedAt = it.startedAt,
                        elapsedTime = it.elapsedTime,
                        distance = it.distance,
                        description = it.description,
                        isPending = it.isPending
                    )
                }
                activitiesMutableLiveData.postValue(list)
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }

    fun insertActivitiesToDbFromServer(list: List<ActivityItem>) {
        viewModelScope.launch {
            try {
                list.map {
                    val activityEntity = ActivityEntity(
                        id = it.stravaId.toString(),
                        activityName = it.activityName,
                        activityType = it.activityType,
                        startedAt = it.startedAt,
                        elapsedTime = it.elapsedTime,
                        distance = it.distance,
                        description = it.description,
                        isPending = false
                    )
                    activitiesRepository.insertActivityModel(activityEntity)
                }
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }

    fun sentPendingActivities(list: List<ActivityEntity>) {
        viewModelScope.launch {
            try {
                activitiesRepository.sentPendingActivities(list)
                sentPendingActivitiesLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }

    fun getListOfPendingActivities() {
        viewModelScope.launch {
            try {
                val list = activitiesRepository.getListOfPendingActivities()
                pendingActivitiesMutableLiveData.postValue(list)
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }

    fun getAthlete(): Athlete? {
        return athleteManager.getAthlete()
    }
}