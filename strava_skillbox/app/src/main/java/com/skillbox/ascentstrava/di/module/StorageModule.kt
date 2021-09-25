package com.skillbox.ascentstrava.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs"
    }
}