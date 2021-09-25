package com.skillbox.ascentstrava.presentation.onboarding.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.onboarding.OnboardingFragment
import dagger.Component

@OnboardingScope
@Component(dependencies = [AppComponent::class])
interface OnboardingComponent {

    fun inject(onboardingFragment: OnboardingFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): OnboardingComponent
    }
}