package com.skillbox.ascentstrava.presentation.activities.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.notification.NotificationRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityMapper
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import com.skillbox.ascentstrava.utils.isNetworkError
import com.skillbox.ascentstrava.utils.isServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CreateActivityViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val activityMapper: ActivityMapper,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val successLiveEvent = SingleLiveEvent<Unit>()
    private val errorLiveEvent = SingleLiveEvent<Int>()

    val successLiveData: LiveData<Unit>
        get() = successLiveEvent

    val errorLiveData: LiveData<Int>
        get() = errorLiveEvent

    fun createActivity(
        name: String?,
        type: String?,
        startedAt: String?,
        elapsedTime: Int?,
        distance: Float?,
        description: String?
    ) {
        viewModelScope.launch {
            val uniqueId = UUID.randomUUID().toString()
            val activityEntity = ActivityEntity(
                id = uniqueId,
                name = name,
                type = type,
                startedAt = startedAt?.let { activityMapper.dateStringToMillis(it) },
                elapsedTime = elapsedTime,
                distance = distance,
                description = description,
                isPending = true
            )

            flow {
                emit(activityEntity)
            }
                .onEach {
                    activitiesRepository.insertActivityToDb(it)
                }
                .catch {
                    Timber.e("???? ?????????????? ???????????????? ???????????????????? ?? ???????? ????????????")
                }
                .map {
                    val activityModel = activityMapper.mapEntityToModel(activityEntity)
                    activitiesRepository.createActivity(activityModel)
                }
                .flowOn(Dispatchers.IO)
                .onEach {
                    activitiesRepository.updateEntityByUniqueId(it, uniqueId)
                }
                .catch {
                    if (it.isNetworkError() || it.isServerError()) {
                        successLiveEvent.postValue(Unit)
                    } else {
                        activitiesRepository.deleteEntityByUniqueId(uniqueId)
                        errorLiveEvent.postValue(R.string.failed_to_add_activity)
                    }
                }
                .catch {
                    Timber.e("???? ?????????????? ???????????????? ???????????????????? ?? ???????? ????????????")
                }
                .collect {
                    successLiveEvent.postValue(Unit)
                    notificationRepository.createDelayNotification()
                }
        }
    }
}