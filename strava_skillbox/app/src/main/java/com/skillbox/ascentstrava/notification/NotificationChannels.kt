package com.skillbox.ascentstrava.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {

    const val TRAIN_CHANNEL_ID = "train"
    private const val TRAIN_CHANNEL_NAME = "Train"
    private const val TRAIN_CHANNEL_DESCRIPTION = "To train channel"


    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createTrainChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTrainChannel(context: Context) {
        val name = TRAIN_CHANNEL_NAME
        val channelDescription = TRAIN_CHANNEL_DESCRIPTION
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(TRAIN_CHANNEL_ID, name, priority).apply {
            description = channelDescription
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}