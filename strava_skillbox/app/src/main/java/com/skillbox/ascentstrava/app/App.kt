package com.skillbox.ascentstrava.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.skillbox.ascentstrava.BuildConfig
import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        appComponent = initDagger()

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDeath()
                    .build()
            )
        }
    }

    private fun initDagger(): AppComponent {
        return DaggerAppComponent.factory()
            .create(this)
    }
}
val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }