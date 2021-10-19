package com.skillbox.ascentstrava.presentation.activities.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skillbox.ascentstrava.data.db.ActivitiesContract
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = ActivitiesContract.TABLE_NAME
)
@JsonClass(generateAdapter = true)
data class ActivityModel(
    @PrimaryKey
    @ColumnInfo(name = ActivitiesContract.Columns.BD_ID)
    val bdId: String,
    @ColumnInfo(name = ActivitiesContract.Columns.STRAVA_ID)
    @Json(name = "id")
    val stravaId: Long?,
    @ColumnInfo(name = ActivitiesContract.Columns.NAME)
    @Json(name = "name")
    val activityName: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.TYPE)
    @Json(name = "type")
    val activityType: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.STARTED_AT)
    @Json(name = "start_date_local")
    val startedAt: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.ELAPSED_TIME)
    @Json(name = "elapsed_time")
    val elapsedTime: Int?,
    @ColumnInfo(name = ActivitiesContract.Columns.DISTANCE)
    @Json(name = "distance")
    val distance: Float?,
    @ColumnInfo(name = ActivitiesContract.Columns.DESCRIPTION)
    @Json(name = "description")
    val description: String?
)
