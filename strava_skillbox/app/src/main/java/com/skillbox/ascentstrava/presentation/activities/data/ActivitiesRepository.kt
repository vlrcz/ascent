package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(
    private val stravaApi: StravaApi,
    private val activitiesDao: ActivitiesDao
) {

    suspend fun createActivity(activityModel: ActivityModel): ActivityModel {
        return withContext(Dispatchers.IO) {
            stravaApi.createActivity(
                name = activityModel.activityName,
                activityType = activityModel.activityType,
                startedAt = activityModel.startedAt,
                elapsedTime = activityModel.elapsedTime,
                distance = activityModel.distance,
                description = activityModel.description
            )
        }
    }

    suspend fun getActivities(): List<ActivityModel> {
        return withContext(Dispatchers.IO) {
            stravaApi.getActivities()
        }
    }

    suspend fun insertActivityModel(activityModel: ActivityModel) {
        activitiesDao.insertActivityModel(activityModel)
    }

    suspend fun getActivitiesFromDb(): List<ActivityModel> {
        return activitiesDao.getActivities()
    }
}