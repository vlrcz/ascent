package com.skillbox.ascentstrava.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.skillbox.ascentstrava.presentation.auth.AuthFragment
import com.skillbox.ascentstrava.presentation.auth.AuthViewModel
import com.skillbox.ascentstrava.presentation.onboarding.OnboardingFragment
import com.skillbox.ascentstrava.storage.StorageViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(onboardingFragment: OnboardingFragment)
    fun inject(authFragment: AuthFragment)
    fun storageViewModel(): StorageViewModel
    fun authViewModel(): AuthViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}