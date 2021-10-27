package com.skillbox.ascentstrava.presentation.activities.create

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityMapper
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class CreateActivityViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val activityMapper: ActivityMapper
) : ViewModel() {

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val insertSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<String>()
    private val toastLiveEvent = SingleLiveEvent<Int>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    val insertSuccessLiveData: LiveData<Unit>
        get() = insertSuccessLiveEvent

    val saveErrorLiveData: LiveData<String>
        get() = saveErrorLiveEvent

    val toastLiveData: LiveData<Int>
        get() = toastLiveEvent

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
                name,
                type,
                startedAt,
                elapsedTime,
                distance,
                description,
                true
            )

            flow {
                emit(activityEntity)
            }
                .onEach {
                    activitiesRepository.insertActivityToDb(it)
                }
                .catch {
                    Timber.d("Не удалось добавить активность в базу данных")
                }
                .map {
                    val activityModel = activityMapper.mapEntityToModel(activityEntity)
                    activitiesRepository.createActivity(activityModel)
                }
                .flowOn(Dispatchers.IO)
                .catch {
                    if (it is UnknownHostException) {
                        insertSuccessLiveEvent.postValue(Unit)
                        toastLiveEvent.postValue(R.string.no_connection_add_to_db)
                    } else {
                        activitiesRepository.deleteEntityByUniqueId(uniqueId)
                    }
                }
                .onEach {
                    activitiesRepository.updateEntityByUniqueId(it, uniqueId)
                }
                .catch {
                    Timber.d("Не удалось обновить активность в базе данных")
                }
                .collect {
                    saveSuccessLiveEvent.postValue(Unit)
                }
        }
    }
}