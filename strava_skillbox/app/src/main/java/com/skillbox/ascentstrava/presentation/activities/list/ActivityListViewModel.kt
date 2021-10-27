package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityItem
import com.skillbox.ascentstrava.presentation.activities.data.ActivityMapper
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class ActivityListViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager,
    private val connectionManager: ConnectionManager,
    private val activityMapper: ActivityMapper
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityItem>>()
    private val errorLiveEvent = SingleLiveEvent<String>()
    private val networkMutableLiveData = connectionManager.observeNetworkState()
    private val sentSuccessLiveEvent = SingleLiveEvent<Unit>()

    val isNetworkAvailable: LiveData<Boolean>
        get() = networkMutableLiveData

    val activitiesLiveData: LiveData<List<ActivityItem>>
        get() = activitiesMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorLiveEvent

    val sentSuccessLiveData: LiveData<Unit>
        get() = sentSuccessLiveEvent

    fun loadList() {
        viewModelScope.launch {
            flow {
                emit(activitiesRepository.getActivities())
            }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    if (throwable is UnknownHostException) {
                        try {
                            val listOfEntity = activitiesRepository.getActivitiesFromDb()
                            val listOfItems = listOfEntity.map { entity ->
                                activityMapper.mapEntityToItem(entity)
                            }.sortedByDescending { it.startedAt }
                            activitiesMutableLiveData.postValue(listOfItems)
                        } catch (t: Throwable) {
                            errorLiveEvent.postValue("Ошибка загрузки")
                        }
                    } else {
                        errorLiveEvent.postValue("Ошибка загрузки")
                    }
                }
                .onEach {
                    val listOfItems = it.map { model ->
                        activityMapper.mapModelToItem(model)
                    }
                    activitiesMutableLiveData.postValue(listOfItems)
                }
                .onEach {
                    val listOfEntities = it.map { model ->
                        activityMapper.mapModelToEntity(model)
                    }
                    listOfEntities.map { entity ->
                        activitiesRepository.insertActivityToDb(entity)
                    }
                }
                .collect()
        }
    }

    fun sentPendingActivities() {
        viewModelScope.launch {
            flow {
                emit(activitiesRepository.getListOfPendingActivities())
            }
                .catch { Timber.d("Get list of pending activities error") }
                .onEach {
                    it.map { entity ->
                        val model = activityMapper.mapEntityToModel(entity)
                        activitiesRepository.createActivity(model)
                    }
                }
                .catch { Timber.d("Create pending activities error") }
                .onEach {
                    it.map { entity ->
                        val model = activityMapper.mapEntityToModel(entity)
                        activitiesRepository.updateEntityByUniqueId(model, entity.id)
                    }
                }
                .collect {
                    sentSuccessLiveEvent.postValue(Unit)
                }
        }
    }

    fun getAthlete(): Athlete? {
        return athleteManager.getAthlete()
    }
}