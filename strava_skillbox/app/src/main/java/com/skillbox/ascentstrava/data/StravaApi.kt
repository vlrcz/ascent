package com.skillbox.ascentstrava.data

import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface StravaApi {

    @GET("api/v3/athlete")
    suspend fun getProfileInfo(): Athlete

    @PUT("api/v3/athlete")
    suspend fun changeAthleteWeight(
        @Body weight: UpdateRequestBody
    )

    @POST("api/v3/activities")
    suspend fun createActivity(
        @Field("name") name: String
    )

    @POST("oauth/deauthorize")
    suspend fun deAuthorize(
        @Query("access_token") accessToken: String
    )
}