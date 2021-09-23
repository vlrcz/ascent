package com.skillbox.ascentstrava.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule {

    @Singleton
    @Provides
    fun providesContext(application: Application): Context {
        return application
    }
}