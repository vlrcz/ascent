package com.skillbox.ascentstrava.app

import android.app.Application
import android.content.Context
import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.di.DaggerAppComponent
import com.skillbox.ascentstrava.presentation.athlete.data.AthleteAppInitializer
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var athleteAppInitializer: AthleteAppInitializer

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        appComponent = DaggerAppComponent.factory()
            .create(this, this)
        appComponent.inject(this)

        athleteAppInitializer.init()

        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDeath()
                    .build()
            )
        }*/
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }