package com.skillbox.ascentstrava.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepository @Inject constructor(
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
}