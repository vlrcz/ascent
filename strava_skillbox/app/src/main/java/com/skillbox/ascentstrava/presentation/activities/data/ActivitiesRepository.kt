package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import com.skillbox.ascentstrava.data.db.ActivityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(
    private val api: StravaApi,
    private val activitiesDao: ActivitiesDao
) {
    suspend fun createActivity(activityModel: ActivityModel): ActivityModel {
        return withContext(Dispatchers.IO) {
            api.createActivity(
                name = activityModel.name,
                activityType = activityModel.type,
                startedAt = activityModel.startedAt,
                elapsedTime = activityModel.elapsedTime,
                distance = activityModel.distance,
                description = activityModel.description
            )
        }
    }

    suspend fun getActivities(page: Int, items: Int): List<ActivityModel> {
        return withContext(Dispatchers.IO) {
            api.getActivities(page, items)
        }
    }

    suspend fun insertActivityToDb(activityEntity: ActivityEntity) {
        withContext(Dispatchers.IO) {
            activitiesDao.insertActivityToDb(activityEntity)
        }
    }

    suspend fun insertListOfActivityToDb(list: List<ActivityEntity>) {
        withContext(Dispatchers.IO) {
            activitiesDao.insertListOfActivityToDb(list)
        }
    }

    suspend fun getActivitiesFromDb(limit: Int, offset: Int): List<ActivityEntity> {
        return withContext(Dispatchers.IO) {
            activitiesDao.getActivities(limit, offset)
        }
    }

    suspend fun updateEntityByUniqueId(activityModel: ActivityModel, uniqueId: String) {
        withContext(Dispatchers.IO) {
            activitiesDao.updateEntityByModel(activityModel.id.toString(), uniqueId, false)
        }
    }

    suspend fun deleteEntityByUniqueId(uniqueId: String) {
        withContext(Dispatchers.IO) {
            activitiesDao.deleteEntityByUniqueId(uniqueId)
        }
    }

    suspend fun getListOfPendingActivities(): List<ActivityEntity> {
        return withContext(Dispatchers.IO) {
            activitiesDao.getListOfPendingActivities(true)
        }
    }
}