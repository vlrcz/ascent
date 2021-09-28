package com.skillbox.ascentstrava.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import net.openid.appauth.AuthorizationService

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