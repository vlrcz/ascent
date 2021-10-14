package com.skillbox.ascentstrava.presentation.profile.data

import android.content.SharedPreferences
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.profile.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val stravaApi: StravaApi,
    private val authManager: AuthManager
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

    suspend fun deAuthorize() {
        withContext(Dispatchers.IO) {
            authManager.receiveAccessToken()?.let { stravaApi.deAuthorize(it) }
        }
    }
}