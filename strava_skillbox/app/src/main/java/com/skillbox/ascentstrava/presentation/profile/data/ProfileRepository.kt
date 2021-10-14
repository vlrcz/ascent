package com.skillbox.ascentstrava.presentation.profile.data

import android.net.Uri
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.profile.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            val accessToken = authManager.receiveAccessToken()
            if (accessToken != null) {
                val url = Uri.parse(AuthConfig.BASE_URL + AuthConfig.LOGOUT)
                    .buildUpon()
                    .appendQueryParameter(AuthConfig.ACCESS_TOKEN, accessToken)
                    .build()

                stravaApi.logout(url.toString())
            }
        }
    }
}