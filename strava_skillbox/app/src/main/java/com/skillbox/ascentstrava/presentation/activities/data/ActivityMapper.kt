package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.presentation.athlete.Athlete
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ActivityMapper @Inject constructor() {

    companion object {
        private const val KM = "km"
        private const val MIN = "m"
    }

    fun mapEntityToModel(activityEntity: ActivityEntity): ActivityModel {
        return ActivityModel(
            id = null,
            name = activityEntity.name,
            type = activityEntity.type,
            startedAt = activityEntity.startedAt?.let { millisToModelDate(it) },
            elapsedTime = activityEntity.elapsedTime,
            distance = activityEntity.distance,
            description = activityEntity.description
        )
    }

    fun mapEntityToItem(activityEntity: ActivityEntity, athlete: Athlete?): ActivityItem {
        return ActivityItem(
            uniqueId = activityEntity.id,
            stravaId = if (activityEntity.isPending) null else activityEntity.id.toLongOrNull(),
            athleteName = athlete?.fullName.orEmpty(),
            athleteImage = athlete?.photoUrl.orEmpty(),
            name = activityEntity.name,
            type = activityEntity.type,
            startedAt = activityEntity.startedAt?.let { millisToItemDate(it) },
            elapsedTime = "${activityEntity.elapsedTime?.div(60)}$MIN",
            distance = "${activityEntity.distance?.toInt()?.div(1000)} $KM",
            description = activityEntity.description,
            isPending = activityEntity.isPending
        )
    }

    fun mapModelToEntity(activityModel: ActivityModel): ActivityEntity {
        return ActivityEntity(
            id = activityModel.id.toString(),
            name = activityModel.name,
            type = activityModel.type,
            startedAt = activityModel.startedAt?.let { dateStringToMillis(it) },
            elapsedTime = activityModel.elapsedTime,
            distance = activityModel.distance,
            description = activityModel.description,
            false
        )
    }

    fun dateStringToMillis(date: String): Long {
        val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
        return currentFormat.parse(date).time
    }

    private fun millisToItemDate(millis: Long): String {
        val targetFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.ROOT)
        return targetFormat.format(millis)
    }

    private fun millisToModelDate(millis: Long): String {
        val targetFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
        return targetFormat.format(millis)
    }
}