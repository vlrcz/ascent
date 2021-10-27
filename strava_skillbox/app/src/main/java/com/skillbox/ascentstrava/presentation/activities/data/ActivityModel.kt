package com.skillbox.ascentstrava.presentation.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityModel(
    val id: Long?,
    val name: String?,
    val type: String?,
    @Json(name = "start_date_local")
    val startedAt: String?,
    @Json(name = "elapsed_time")
    val elapsedTime: Int?,
    val distance: Float?,
    val description: String?
)
