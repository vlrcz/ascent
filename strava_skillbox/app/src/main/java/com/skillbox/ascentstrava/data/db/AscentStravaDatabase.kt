package com.skillbox.ascentstrava.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skillbox.ascentstrava.presentation.athlete.Athlete
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteDao

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