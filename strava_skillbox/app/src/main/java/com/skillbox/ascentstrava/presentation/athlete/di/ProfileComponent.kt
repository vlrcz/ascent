package com.skillbox.ascentstrava.presentation.athlete.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.athlete.AthleteFragment
import dagger.Component

@ProfileScope
@Component(
    dependencies = [AppComponent::class]
)
interface ProfileComponent {

    fun inject(athleteFragment: AthleteFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ProfileComponent
    }
}