package com.skillbox.ascentstrava.presentation.activities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Activity(
    @Json(name = "name")
    val activityName: String
)
