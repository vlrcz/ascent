package com.skillbox.ascentstrava.presentation.athlete.data

import android.net.Uri
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.presentation.athlete.Athlete
import javax.inject.Inject

class AthleteRepository @Inject constructor(
    private val api: StravaApi,
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
}
