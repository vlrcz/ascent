package com.skillbox.ascentstrava.data

import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface StravaApi {

    @GET("athlete")
    suspend fun getProfileInfo(): Athlete

    @PUT("athlete")
    suspend fun changeAthleteWeight(
        @Body weight: UpdateRequestBody
    )

    @FormUrlEncoded
    @POST("activities")
    suspend fun createActivity(
        @Field("name") name: String?,
        @Field("type") activityType: String?,
        @Field("start_date_local") startedAt: String?,
        @Field("elapsed_time") elapsedTime: Int?,
        @Field("distance") distance: Float?,
        @Field("description") description: String?
    ): ActivityModel

    @POST
    suspend fun logout(
        @Url url: String
    )

    @GET("athlete/activities")
    suspend fun getActivities(): List<ActivityModel>
}