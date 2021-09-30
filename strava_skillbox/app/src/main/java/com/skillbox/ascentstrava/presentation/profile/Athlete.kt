package com.skillbox.ascentstrava.presentation.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Athlete(
    @Json(name = "id")
    val id: Long,
    @Json(name = "username")
    val userName: String,
    @Json(name = "firstname")
    val firstName: String,
    @Json(name = "lastname")
    val lastName: String,
    @Json(name = "friend_count")
    val friends: Long,
    @Json(name = "follower_count")
    val followers: Long,
    @Json(name = "sex")
    val gender: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "weight")
    val weight: Long
)