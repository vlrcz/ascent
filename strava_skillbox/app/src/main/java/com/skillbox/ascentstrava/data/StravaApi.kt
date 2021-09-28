package com.skillbox.ascentstrava.data

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface StravaApi {

    @FormUrlEncoded
    @POST("auth/refresh-token")
    suspend fun refreshAccessToken(
        @Field("refresh_token") refreshToken: String?
    ): TokenResponse
}