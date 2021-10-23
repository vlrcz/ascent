package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import com.skillbox.ascentstrava.data.db.ActivityEntity
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

    suspend fun getActivities(): List<ActivityItem> {
        return withContext(Dispatchers.IO) {
            stravaApi.getActivities()
        }
    }

    suspend fun insertActivityModel(activityEntity: ActivityEntity) {
        activitiesDao.insertActivityModel(activityEntity)
    }

    suspend fun getActivitiesFromDb(): List<ActivityEntity> {
        return activitiesDao.getActivities()
    }

    suspend fun updateEntityByUniqueId(activityModel: ActivityModel, uniqueId: String) {
        val stravaId = activityModel.stravaId.toString()
        activitiesDao.updateEntityByModel(stravaId, uniqueId, false)
    }

    suspend fun deleteEntityByUniqueId(uniqueId: String) {
        activitiesDao.deleteEntityByUniqueId(uniqueId)
    }

    suspend fun sentPendingActivities(list: List<ActivityEntity>) {
        withContext(Dispatchers.IO) {
            list.map {
                val activityModel = ActivityModel(
                    stravaId = null,
                    activityName = it.activityName,
                    activityType = it.activityType,
                    startedAt = it.startedAt,
                    elapsedTime = it.elapsedTime,
                    distance = it.distance,
                    description = it.description
                )
                val responseActivityModel = createActivity(activityModel)
                updateEntityByUniqueId(responseActivityModel, it.id)
            }
        }
    }

    suspend fun getListOfPendingActivities(): List<ActivityEntity> {
        return activitiesDao.getListOfPendingActivities(true)
    }
}