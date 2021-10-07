package com.skillbox.ascentstrava.presentation.profile.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateRequestBody(
    val weight: Float
)
