package com.skillbox.ascentstrava.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.db.ActivitiesDao
import com.skillbox.ascentstrava.di.module.AuthModule
import com.skillbox.ascentstrava.di.module.DatabaseModule
import com.skillbox.ascentstrava.di.module.NetworkModule
import com.skillbox.ascentstrava.di.module.StorageModule
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import net.openid.appauth.AuthorizationService

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, AuthModule::class, DatabaseModule::class])
interface AppComponent {

    fun context(): Context
    fun authManager(): AuthManager
    fun sharedPrefs(): SharedPreferences
    fun application(): Application
    fun authService(): AuthorizationService
    fun stravaApi(): StravaApi
    fun athleteManager(): AthleteManager
    fun activitiesDao(): ActivitiesDao

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}