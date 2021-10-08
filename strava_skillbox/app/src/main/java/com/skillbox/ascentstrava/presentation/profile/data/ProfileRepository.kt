package com.skillbox.ascentstrava.presentation.profile.data

import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.profile.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val stravaApi: StravaApi
) {
    suspend fun getProfileInfo(): Athlete {
        return withContext(Dispatchers.IO) {
            stravaApi.getProfileInfo()
        }
    }

    suspend fun changeAthleteWeight(weight: UpdateRequestBody) {
        withContext(Dispatchers.IO) {
            stravaApi.changeAthleteWeight(weight)
        }
    }
}