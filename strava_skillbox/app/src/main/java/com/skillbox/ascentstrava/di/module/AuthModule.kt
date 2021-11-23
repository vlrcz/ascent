package com.skillbox.ascentstrava.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import net.openid.appauth.AuthorizationService
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun providesAuthService(
        application: Application
    ): AuthorizationService {
        return AuthorizationService(application)
    }
}