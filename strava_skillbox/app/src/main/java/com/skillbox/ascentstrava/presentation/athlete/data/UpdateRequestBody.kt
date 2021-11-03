package com.skillbox.ascentstrava.presentation.athlete.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateRequestBody(
    val weight: Float
)
