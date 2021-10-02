package com.skillbox.ascentstrava.data

import com.squareup.moshi.Json

data class RefreshRequest(
    @Json(name = "client_id")
    val clientId: Int,
    @Json(name = "client_secret")
    val clientSecret: String,
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "grant_type")
    val grantType: String
)