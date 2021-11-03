package com.skillbox.ascentstrava.presentation.athlete.data

import android.net.Uri
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.athlete.Athlete
import javax.inject.Inject

class AthleteRepository @Inject constructor(
    private val api: StravaApi,
    private val authManager: AuthManager,
    private val dao: AthleteDao
) {
    suspend fun fetchAthlete(): Athlete {
        return api.fetchAthlete()
    }

    suspend fun fetchLocalAthlete(): Athlete? {
        return dao.fetchAthlete()
    }

    suspend fun insertAthlete(athlete: Athlete) {
        dao.insertAthlete(athlete)
    }

    suspend fun clearAthlete() {
        dao.clearAthlete()
    }

    suspend fun changeAthleteWeight(weight: UpdateRequestBody) {
        api.changeAthleteWeight(weight)
    }

    suspend fun logout() {
        val accessToken = authManager.fetchAccessToken()
        if (accessToken != null) {
            val url = Uri.parse(AuthConfig.BASE_URL + AuthConfig.LOGOUT)
                .buildUpon()
                .appendQueryParameter(AuthConfig.ACCESS_TOKEN, accessToken)
                .build()

            api.logout(url.toString())
        }
    }
}
