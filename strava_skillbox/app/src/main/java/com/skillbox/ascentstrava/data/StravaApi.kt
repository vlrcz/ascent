package com.skillbox.ascentstrava.data

import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface StravaApi {

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