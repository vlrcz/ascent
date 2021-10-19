package com.skillbox.ascentstrava.di.module

import android.app.Application
import androidx.room.Room
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import com.skillbox.ascentstrava.data.db.AscentStravaDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application): AscentStravaDatabase {
        return Room.databaseBuilder(
            application,
            AscentStravaDatabase::class.java,
            AscentStravaDatabase.DB_NAME
        )
            .build()
    }

    @Provides
    fun providesActivitiesDao(db: AscentStravaDatabase): ActivitiesDao {
        return db.activitiesDao()
    }
}