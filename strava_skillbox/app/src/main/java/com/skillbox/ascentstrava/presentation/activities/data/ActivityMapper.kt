package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.data.db.ActivityEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityMapper @Inject constructor() {

    fun mapEntityToModel(activityEntity: ActivityEntity): ActivityModel {
        return ActivityModel(
            id = null,
            name = activityEntity.name,
            type = activityEntity.type,
            startedAt = activityEntity.startedAt,
            elapsedTime = activityEntity.elapsedTime,
            distance = activityEntity.distance,
            description = activityEntity.description
        )
    }

    fun mapModelToItem(activityModel: ActivityModel): ActivityItem {
        return ActivityItem(
            uniqueId = null,
            stravaId = activityModel.id,
            name = activityModel.name,
            type = activityModel.type,
            startedAt = activityModel.startedAt?.let { bindDate(it) },
            elapsedTime = activityModel.elapsedTime?.div(60),
            distance = activityModel.distance?.toInt()?.div(1000),
            description = activityModel.description,
            false
        )
    }

    fun mapEntityToItem(activityEntity: ActivityEntity): ActivityItem {
        return ActivityItem(
            uniqueId = activityEntity.id,
            stravaId = null,
            name = activityEntity.name,
            type = activityEntity.type,
            startedAt = activityEntity.startedAt?.let { bindDate(it) },
            elapsedTime = activityEntity.elapsedTime?.div(60),
            distance = activityEntity.distance?.toInt()?.div(1000),
            description = activityEntity.description,
            isPending = activityEntity.isPending
        )
    }

    private fun bindDate(date: String): String {
        val currentFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.ROOT
        )
        val targetFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.ROOT)
        return targetFormat.format(currentFormat.parse(date))
    }

    fun mapModelToEntity(activityModel: ActivityModel): ActivityEntity {
        return ActivityEntity(
            id = activityModel.id.toString(),
            name = activityModel.name,
            type = activityModel.type,
            startedAt = activityModel.startedAt,
            elapsedTime = activityModel.elapsedTime,
            distance = activityModel.distance,
            description = activityModel.description,
            false
        )
    }
}