package com.skillbox.ascentstrava.presentation.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityModel(
    @Json(name = "id")
    val id: Long?,
    @Json(name = "name")
    val activityName: String?,
    @Json(name = "type")
    val activityType: String?,
    @Json(name = "start_date_local")
    val startedAt: String?,
    @Json(name = "elapsed_time")
    val elapsedTime: Int?,
    @Json(name = "distance")
    val distance: Float?,
    @Json(name = "description")
    val description: String?
)
