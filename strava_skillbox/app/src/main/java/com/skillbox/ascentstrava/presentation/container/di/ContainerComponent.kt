package com.skillbox.ascentstrava.presentation.container.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.container.ContainerFragment
import com.skillbox.ascentstrava.presentation.main.di.MainScope
import dagger.Component

@ContainerScope
@Component(
    dependencies = [AppComponent::class]
)
interface ContainerComponent {

    fun inject(containerFragment: ContainerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ContainerComponent
    }
}