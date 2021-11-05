package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityItem
import com.skillbox.ascentstrava.presentation.activities.data.ActivityMapper
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteManager
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import com.skillbox.ascentstrava.utils.isNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ActivityListViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager,
    private val connectionManager: ConnectionManager,
    private val activityMapper: ActivityMapper
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityItem>>()
    private val errorLiveEvent = SingleLiveEvent<Int>()
    private val networkLiveData = MutableLiveData<Boolean>()
    private val sentSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val isNetworkAvailable: LiveData<Boolean>
        get() = networkLiveData

    val activitiesLiveData: LiveData<List<ActivityItem>>
        get() = activitiesMutableLiveData

    val errorLiveData: LiveData<Int>
        get() = errorLiveEvent

    val sentSuccessLiveData: LiveData<Unit>
        get() = sentSuccessLiveEvent

    init {
        viewModelScope.launch {
            connectionManager
                .observeNetworkState()
                .collect {
                    if (it) {
                        sentPendingActivities()
                    }
                    networkLiveData.postValue(it)
                }
        }
    }

    fun loadList() {
        viewModelScope.launch {
            athleteManager
                .observeAthlete()
                .filterNotNull()
                .take(1)
                .onEach {
                    isLoadingLiveData.postValue(true)
                }
                .flatMapConcat { athlete ->
                    flow {
                        emit(activitiesRepository.getActivities())
                    }
                        .map { models ->
                            activitiesRepository.insertListOfActivityToDb(
                                models.map { model ->
                                    activityMapper.mapModelToEntity(model)
                                })
                            models.map { model ->
                                activityMapper.mapModelToItem(model, athlete)
                            }
                                .sortedByDescending { it.date?.time }
                        }
                        .catch { throwable ->
                            if (throwable.isNetworkError()) {
                                try {
                                    val listFromDb = activitiesRepository.getActivitiesFromDb()
                                        .map { entity ->
                                            activityMapper.mapEntityToItem(entity, athlete)
                                        }
                                        .sortedByDescending { it.date?.time }
                                    emit(listFromDb)
                                } catch (t: Throwable) {
                                    errorLiveEvent.postValue(R.string.download_error)
                                }
                            } else {
                                errorLiveEvent.postValue(R.string.download_error)
                            }
                        }
                        .flowOn(Dispatchers.IO)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    isLoadingLiveData.postValue(false)
                    activitiesMutableLiveData.postValue(it)
                }

        }
    }

    private fun sentPendingActivities() {
        viewModelScope.launch {
            flow {
                emit(activitiesRepository.getListOfPendingActivities())
            }
                .filter { it.isNotEmpty() }
                .catch { Timber.e("Get list of pending activities error") }
                .onEach {
                    it.map { entity ->
                        val model = activitiesRepository.createActivity(
                            activityMapper.mapEntityToModel(entity)
                        )
                        try {
                            activitiesRepository.updateEntityByUniqueId(model, entity.id)
                        } catch (t: Throwable) {
                            Timber.e("Update entity by uniqueid error")
                        }
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { Timber.e("Create pending activities error") }
                .collect {
                    sentSuccessLiveEvent.postValue(Unit)
                }
        }
    }
}