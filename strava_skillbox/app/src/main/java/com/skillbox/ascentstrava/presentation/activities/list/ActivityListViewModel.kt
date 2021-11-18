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
import com.skillbox.ascentstrava.presentation.activities.data.PendingActivitiesManager
import com.skillbox.ascentstrava.presentation.athlete.Athlete
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteManager
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import com.skillbox.ascentstrava.utils.isNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityListViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager,
    private val connectionManager: ConnectionManager,
    private val activityMapper: ActivityMapper,
    private val pendingActivitiesManager: PendingActivitiesManager
) : ViewModel() {

    private val activitiesMutableLiveData = MutableLiveData<List<ActivityItem>>()
    private val errorLiveEvent = SingleLiveEvent<Int>()
    private val networkLiveData = MutableLiveData<Boolean>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val loadFlow = MutableSharedFlow<Boolean>(replay = 1)

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val isNetworkAvailable: LiveData<Boolean>
        get() = networkLiveData

    val activitiesLiveData: LiveData<List<ActivityItem>>
        get() = activitiesMutableLiveData

    val errorLiveData: LiveData<Int>
        get() = errorLiveEvent

    init {
        viewModelScope.launch(Dispatchers.Default) {
            connectionManager
                .observeNetworkState()
                .collect {
                    networkLiveData.postValue(it)
                }
        }

        viewModelScope.launch(Dispatchers.Default) {
            pendingActivitiesManager
                .observeSentPending()
                .collect {
                    if (it) {
                        loadList()
                    }
                }
        }

        viewModelScope.launch(Dispatchers.Default) {
            athleteManager
                .observeAthlete()
                .filterNotNull()
                .combine(loadFlow) { athlete, _ ->
                    athlete
                }
                .flatMapLatest { athlete ->
                    fetchActivitiesFlow(athlete)
                }
                .collect {
                    isLoadingLiveData.postValue(false)
                    activitiesMutableLiveData.postValue(it)
                }
        }
    }

    fun loadList() {
        isLoadingLiveData.postValue(true)
        loadFlow.tryEmit(true)
    }

    private fun fetchActivitiesFlow(athlete: Athlete): Flow<List<ActivityItem>> {
        return flow { emit(activitiesRepository.getActivities()) }
            .map { models ->
                activitiesRepository.insertListOfActivityToDb(
                    models.map { model ->
                        activityMapper.mapModelToEntity(model)
                    }
                )
                models
                    .map { model ->
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
            .map {
                activitiesRepository.getActivitiesFromDb()
                    .map { entity ->
                        activityMapper.mapEntityToItem(entity, athlete)
                    }
                    .sortedByDescending { it.date?.time }
            }
            .flowOn(Dispatchers.IO)
    }
}