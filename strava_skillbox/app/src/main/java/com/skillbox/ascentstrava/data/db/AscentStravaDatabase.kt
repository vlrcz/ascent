package com.skillbox.ascentstrava.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel

@Database(
    entities = [
        ActivityModel::class
    ], version = AscentStravaDatabase.DB_VERSION
)
abstract class AscentStravaDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "ascent-strava-database"
    }
}