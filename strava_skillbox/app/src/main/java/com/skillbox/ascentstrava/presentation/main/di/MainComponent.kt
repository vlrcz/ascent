package com.skillbox.ascentstrava.presentation.main.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.main.ContainerFragment
import com.skillbox.ascentstrava.presentation.main.MainFragment
import dagger.Component

@MainScope
@Component(
    dependencies = [AppComponent::class]
)
interface MainComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(containerFragment: ContainerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): MainComponent
    }
}