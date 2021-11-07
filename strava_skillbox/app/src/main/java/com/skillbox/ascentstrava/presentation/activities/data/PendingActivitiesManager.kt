package com.skillbox.ascentstrava.presentation.activities.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PendingActivitiesManager @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val activityMapper: ActivityMapper
) {
    private val pendingActivityListener = MutableStateFlow(false)

    suspend fun sentPendingActivities() {
        flow { emit(activitiesRepository.getListOfPendingActivities()) }
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
                if (it.isNotEmpty()) {
                    pendingActivityListener.value = true
                }
            }
    }

    fun observeSentPending(): Flow<Boolean> = pendingActivityListener
}