package com.skillbox.ascentstrava.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skillbox.ascentstrava.R

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(
    context, params
) {

    companion object {
        const val NOTIFICATION_WORK_ID = "notification work"
        const val TRAIN_NOTIFICATION_TITLE = "Training"
        const val TRAIN_NOTIFICATION_TEXT = "It's time to train"
        const val NOTIFICATION_ID = 1111
    }

    override suspend fun doWork(): Result {
        showTrainNotification()
        return Result.success()
    }

    private fun showTrainNotification() {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.nav_graph_nested)
            .setDestination(R.id.activitiesFragment)
            .createPendingIntent()

        val notification =
            NotificationCompat.Builder(applicationContext, NotificationChannels.TRAIN_CHANNEL_ID)
                .setContentTitle(TRAIN_NOTIFICATION_TITLE)
                .setContentText(TRAIN_NOTIFICATION_TEXT)
                .setSmallIcon(R.drawable.ic_activities)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_ID, notification)
    }
}