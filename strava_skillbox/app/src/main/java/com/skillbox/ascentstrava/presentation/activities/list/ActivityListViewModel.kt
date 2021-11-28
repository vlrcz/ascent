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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityListViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val athleteManager: AthleteManager,
    private val connectionManager: ConnectionManager,
    private val activityMapper: ActivityMapper,
    private val pendingActivitiesManager: PendingActivitiesManager
) : ViewModel() {

    companion object {
        private const val LIMIT_PER_PAGE = 5
    }

    private var state = PagingState(
        limit = LIMIT_PER_PAGE,
        loadingPage = false,
        pageCount = 1,
        itemsList = emptyList(),
        hasMore = true,
        isFirstLoad = false
    )

    private val activitiesMutableLiveData = MutableLiveData<PagingState>()
    private val errorLiveEvent = SingleLiveEvent<Int>()
    private val networkLiveData = MutableLiveData<Boolean>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val loadFlow = MutableSharedFlow<Boolean>(replay = 1)

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val isNetworkAvailable: LiveData<Boolean>
        get() = networkLiveData

    val activitiesLiveData: LiveData<PagingState>
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
                    refresh()
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
                    state.pageCount++
                    isLoadingLiveData.postValue(false)
                    state = state.copy(itemsList = state.itemsList + it)
                    activitiesMutableLiveData.postValue(state)
                    state.loadingPage = false
                }
        }
    }

    fun loadMore() {
        if (!state.loadingPage && state.hasMore) {
            load()
        }
    }

    fun firstLoad() {
        if (!state.isFirstLoad) {
            load()
            state.isFirstLoad = true
        }
    }

    fun refresh() {
        state = state.copy(pageCount = 1, hasMore = true, itemsList = emptyList())
        load()
    }

    private fun load() {
        state.loadingPage = true
        isLoadingLiveData.postValue(true)
        loadFlow.tryEmit(true)
    }

    private fun fetchActivitiesFlow(athlete: Athlete): Flow<List<ActivityItem>> {
        return flow {
            emit(activitiesRepository.getActivities(state.pageCount, state.limit))
        }
            .onEach { models ->
                state.hasMore = models.size >= 5

                activitiesRepository.insertListOfActivityToDb(
                    models.map { model ->
                        activityMapper.mapModelToEntity(model)
                    }
                )
            }
            .catch { throwable ->
                if (throwable.isNetworkError()) {
                    emit(emptyList())
                } else {
                    errorLiveEvent.postValue(R.string.download_error)
                }
            }
            .map {
                activitiesRepository.getActivitiesFromDb(state.limit, state.offset)
                    .map { entity ->
                        activityMapper.mapEntityToItem(entity, athlete)
                    }
            }
            .flowOn(Dispatchers.IO)
    }
}