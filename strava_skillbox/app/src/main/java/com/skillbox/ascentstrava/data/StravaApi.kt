package com.skillbox.ascentstrava.data

import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import retrofit2.http.*

interface StravaApi {

    @FormUrlEncoded
    @POST("auth/refresh-token")
    suspend fun refreshAccessToken(
        @Field("refresh_token") refreshToken: String?
    ): TokenResponse //todo удалить? не используется

    @GET("athlete")
    suspend fun getProfileInfo(): Athlete

    @PUT("athlete")
    suspend fun changeAthleteWeight(
        @Body weight: UpdateRequestBody
    )

    @POST("activities")
    suspend fun createActivity(
        @Field("name") name: String
    )
}