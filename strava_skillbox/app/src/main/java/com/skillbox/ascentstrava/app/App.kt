package com.skillbox.ascentstrava.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.skillbox.ascentstrava.BuildConfig
import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.di.DaggerAppComponent
import com.skillbox.ascentstrava.presentation.activities.data.ActivityAppInitializer
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteAppInitializer
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var athleteAppInitializer: AthleteAppInitializer

    @Inject
    lateinit var activityAppInitializer: ActivityAppInitializer

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
        appComponent = DaggerAppComponent.factory()
            .create(this, this)
        appComponent.inject(this)

        athleteAppInitializer.init()
        activityAppInitializer.init()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }