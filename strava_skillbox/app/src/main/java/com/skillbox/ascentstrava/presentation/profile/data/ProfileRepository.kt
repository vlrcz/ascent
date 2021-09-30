package com.skillbox.ascentstrava.presentation.profile.data

import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.profile.Athlete
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository @Inject constructor(
    private val stravaApi: StravaApi
) {
    suspend fun getProfileInfo(): Athlete {
        return withContext(Dispatchers.IO) {
            stravaApi.getProfileInfo()
        }
    }
}