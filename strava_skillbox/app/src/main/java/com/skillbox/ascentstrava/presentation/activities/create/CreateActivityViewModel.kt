package com.skillbox.ascentstrava.presentation.activities.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class CreateActivityViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {

    private val saveSuccessLiveEvent = SingleLiveEvent<Pair<ActivityModel, String>>()
    private val insertSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<String>()
    private val errorLiveEvent = SingleLiveEvent<String>()
    private val toastLiveEvent = SingleLiveEvent<Int>()

    val saveSuccessLiveData: LiveData<Pair<ActivityModel, String>>
        get() = saveSuccessLiveEvent

    val insertSuccessLiveData: LiveData<Unit>
        get() = insertSuccessLiveEvent

    val saveErrorLiveData: LiveData<String>
        get() = saveErrorLiveEvent

    val errorLiveData: LiveData<String>
        get() = errorLiveEvent

    val toastLiveData: LiveData<Int>
        get() = toastLiveEvent

    fun createActivityModel(activityModel: ActivityModel, uniqueId: String) {
        viewModelScope.launch {
            try {
                val responseActivityModel = activitiesRepository.createActivity(activityModel)
                saveSuccessLiveEvent.postValue(Pair(responseActivityModel, uniqueId))
            } catch (t: Throwable) {
                if (t is UnknownHostException) {
                    toastLiveEvent.postValue(R.string.activity_add_to_db)
                } else {
                    saveErrorLiveEvent.postValue(uniqueId)
                }
            }
        }
    }

    fun updateEntityByUniqueId(responseActivityModel: ActivityModel, uniqueId: String) {
        viewModelScope.launch {
            try {
                activitiesRepository.updateEntityByUniqueId(responseActivityModel, uniqueId)
            } catch (t: Throwable) {
                saveErrorLiveEvent.postValue(t.message)
            }
        }
    }

    fun insertActivityEntityToDb(activityEntity: ActivityEntity) {
        viewModelScope.launch {
            try {
                activitiesRepository.insertActivityModel(activityEntity)
                insertSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }

    fun deleteEntityByUniqueId(uniqueId: String) {
        viewModelScope.launch {
            try {
                activitiesRepository.deleteEntityByUniqueId(uniqueId)
            } catch (t: Throwable) {
                errorLiveEvent.postValue(t.message)
            }
        }
    }
}