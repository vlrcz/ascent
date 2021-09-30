package com.skillbox.ascentstrava.data

import com.skillbox.ascentstrava.presentation.profile.Athlete
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface StravaApi {

    @FormUrlEncoded
    @POST("auth/refresh-token")
    suspend fun refreshAccessToken(
        @Field("refresh_token") refreshToken: String?
    ): TokenResponse

    @GET("athlete")
    suspend fun getProfileInfo(): Athlete

    @POST("activities")
    suspend fun createActivity(
        @Field("name") name: String
    )
}