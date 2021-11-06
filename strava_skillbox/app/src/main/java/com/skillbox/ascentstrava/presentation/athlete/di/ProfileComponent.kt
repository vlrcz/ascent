package com.skillbox.ascentstrava.presentation.athlete.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.athlete.AthleteFragment
import com.skillbox.ascentstrava.presentation.athlete.LogoutDialogFragment
import dagger.Component

@ProfileScope
@Component(
    dependencies = [AppComponent::class]
)
interface ProfileComponent {

    fun inject(athleteFragment: AthleteFragment)
    fun inject(logoutDialogFragment: LogoutDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ProfileComponent
    }
}