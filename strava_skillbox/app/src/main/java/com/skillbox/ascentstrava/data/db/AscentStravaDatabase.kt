package com.skillbox.ascentstrava.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.data.AthleteDao

@Database(
    entities = [
        ActivityEntity::class,
        Athlete::class
    ], version = AscentStravaDatabase.DB_VERSION
)
abstract class AscentStravaDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun athleteDao(): AthleteDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "ascent-strava-database"
    }
}