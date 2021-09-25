package com.skillbox.ascentstrava.presentation.auth.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.auth.AuthFragment
import dagger.Component

@AuthScope
@Component(
    dependencies = [AppComponent::class]
)
interface AuthComponent {

    fun inject(authFragment: AuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): AuthComponent
    }
}

