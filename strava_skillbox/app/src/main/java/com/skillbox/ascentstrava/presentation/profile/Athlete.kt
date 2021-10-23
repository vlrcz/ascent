package com.skillbox.ascentstrava.presentation.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skillbox.ascentstrava.data.db.ActivitiesContract
import com.skillbox.ascentstrava.presentation.profile.data.AthleteContract
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = AthleteContract.TABLE_NAME
)
@JsonClass(generateAdapter = true)
data class Athlete(
    @PrimaryKey
    @ColumnInfo(name = AthleteContract.Columns.ID)
    @Json(name = "id")
    val id: Long,
    @ColumnInfo(name = AthleteContract.Columns.USERNAME)
    @Json(name = "username")
    val userName: String,
    @ColumnInfo(name = AthleteContract.Columns.FIRST_NAME)
    @Json(name = "firstname")
    val firstName: String,
    @ColumnInfo(name = AthleteContract.Columns.LAST_NAME)
    @Json(name = "lastname")
    val lastName: String,
    @ColumnInfo(name = AthleteContract.Columns.FRIENDS)
    @Json(name = "friend")
    val friends: Long?,
    @ColumnInfo(name = AthleteContract.Columns.FOLLOWERS)
    @Json(name = "follower")
    val followers: Long?,
    @ColumnInfo(name = AthleteContract.Columns.GENDER)
    @Json(name = "sex")
    val gender: String,
    @ColumnInfo(name = AthleteContract.Columns.COUNTRY)
    @Json(name = "country")
    val country: String,
    @ColumnInfo(name = AthleteContract.Columns.WEIGHT)
    @Json(name = "weight")
    val weight: Float?,
    @ColumnInfo(name = AthleteContract.Columns.PHOTO_URL)
    @Json(name = "profile_medium")
    val photoUrl: String
)