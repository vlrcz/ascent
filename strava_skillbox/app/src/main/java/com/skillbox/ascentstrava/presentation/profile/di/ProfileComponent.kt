package com.skillbox.ascentstrava.presentation.profile.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.profile.ProfileFragment
import dagger.Component

@ProfileScope
@Component(
    dependencies = [AppComponent::class]
)
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ProfileComponent
    }
}