package com.skillbox.ascentstrava.presentation.activities.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.activities.data.Activity
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateActivityViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<String>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    val saveErrorLiveData: LiveData<String>
        get() = saveErrorLiveEvent


    fun createActivity(activity: Activity) {
        viewModelScope.launch {
            try {
                activitiesRepository.createActivity(activity)
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                saveErrorLiveEvent.postValue(t.message)
            }
        }
    }
}