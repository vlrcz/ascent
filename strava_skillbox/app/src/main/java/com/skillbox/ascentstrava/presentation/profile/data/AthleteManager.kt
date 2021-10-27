package com.skillbox.ascentstrava.presentation.profile.data

import android.content.Context
import android.content.SharedPreferences
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.presentation.profile.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class AthleteManager @Inject constructor(
    private val context: Context,
    private val athleteDao: AthleteDao
) {

    private var athlete: Athlete? = null

    fun getProfileUrl(): String? {
        return if (athlete != null) {
            context.resources.getString(R.string.profile_url) + athlete?.userName
        } else {
            null
        }
    }

    fun getAthlete(): Athlete? {
        return athlete
    }

    fun clearAthlete() {
        athlete = null
    } // todo добавить в кнопку logout

    fun putAthlete(athlete: Athlete) {
        this.athlete = athlete
    }

    /*suspend fun insertAthlete(athlete: Athlete) {
        withContext(Dispatchers.IO) {
            athleteDao.insertAthlete(athlete)
        }
    }

    suspend fun getAthleteFromDb(): Athlete? {
        return withContext(Dispatchers.IO) {
            athleteDao.getAthlete()
        }
    }

    suspend fun clearAthleteInDb() {
        withContext(Dispatchers.IO) {
            athleteDao.clearAthlete()
        }
    }

    suspend fun getProfile(): String? {
        return withContext(Dispatchers.IO) {
            val athlete = athleteDao.getAthlete()
            if (athlete != null) {
                context.resources.getString(R.string.profile_url) + athlete.userName
            } else {
                null
            }
        }
    }*/
}