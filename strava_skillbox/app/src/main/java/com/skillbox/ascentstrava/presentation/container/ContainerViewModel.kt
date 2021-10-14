package com.skillbox.ascentstrava.presentation.container

import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import javax.inject.Inject

class ContainerViewModel @Inject constructor(
    private val athleteManager: AthleteManager
) : ViewModel() {

    fun getProfileUrl(): String? {
        return athleteManager.getProfileUrl()
    }
}