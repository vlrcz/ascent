package com.skillbox.ascentstrava.presentation.activities.list

import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.presentation.activities.data.ActivitiesRepository
import com.skillbox.ascentstrava.presentation.share.data.ContactsRepository
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {


}