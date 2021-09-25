package com.skillbox.ascentstrava.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.di.module.NetworkModule
import com.skillbox.ascentstrava.di.module.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {

    fun context(): Context
    fun authManager(): AuthManager
    fun sharedPrefs(): SharedPreferences

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}