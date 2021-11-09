package com.skillbox.ascentstrava.presentation.activities.data

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.notification.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(
    private val api: StravaApi,
    private val activitiesDao: ActivitiesDao,
    private val context: Context
) {

    fun createDelayNotification() {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                NotificationWorker.NOTIFICATION_WORK_ID,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

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

    suspend fun getActivities(): List<ActivityModel> {
        return withContext(Dispatchers.IO) {
            api.getActivities()
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

    suspend fun getActivitiesFromDb(): List<ActivityEntity> {
        return withContext(Dispatchers.IO) {
            activitiesDao.getActivities()
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