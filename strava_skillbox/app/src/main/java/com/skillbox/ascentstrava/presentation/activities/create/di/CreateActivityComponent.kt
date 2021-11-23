package com.skillbox.ascentstrava.presentation.activities.create.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.activities.create.CreateActivityFragment
import dagger.Component

@CreateActivityScope
@Component(
    dependencies = [AppComponent::class]
)
interface CreateActivityComponent {

    fun inject(createActivityFragment: CreateActivityFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): CreateActivityComponent
    }
}