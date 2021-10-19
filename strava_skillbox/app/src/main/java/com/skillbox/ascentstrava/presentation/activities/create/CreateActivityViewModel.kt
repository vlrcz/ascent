package com.skillbox.ascentstrava.presentation.activities.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateActivityViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<String>()
    private val insertSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val insertErrorLiveEvent = SingleLiveEvent<String>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    val saveErrorLiveData: LiveData<String>
        get() = saveErrorLiveEvent

    val insertSuccessLiveData: LiveData<Unit>
        get() = insertSuccessLiveEvent

    val insertErrorLiveData: LiveData<String>
        get() = insertErrorLiveEvent


    fun createActivityModel(activityModel: ActivityModel) {
        viewModelScope.launch {
            try {
                activitiesRepository.createActivity(activityModel)
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                saveErrorLiveEvent.postValue(t.message)
            }
        }
    }

    fun insertActivityToDb(activityModel: ActivityModel) {
        viewModelScope.launch {
            try {
                activitiesRepository.insertActivityModel(activityModel)
                insertSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                insertErrorLiveEvent.postValue(t.message)
            }
        }
    }
}