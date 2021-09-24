package com.skillbox.ascentstrava.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.skillbox.ascentstrava.presentation.onboarding.OnboardingFragment
import com.skillbox.ascentstrava.storage.StorageViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun inject(onboardingFragment: OnboardingFragment)
    fun storageViewModel(): StorageViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}