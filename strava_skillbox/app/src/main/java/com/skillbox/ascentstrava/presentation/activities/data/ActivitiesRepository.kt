package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.StravaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(private val stravaApi: StravaApi) {

    suspend fun createActivity(activity: Activity) {
        withContext(Dispatchers.IO) {
            stravaApi.createActivity(
                name = activity.activityName,
                activityType = activity.activityType,
                startedAt = activity.startedAt,
                elapsedTime = activity.elapsedTime,
                distance = activity.distance,
                description = activity.description
            )
        }
    }
}