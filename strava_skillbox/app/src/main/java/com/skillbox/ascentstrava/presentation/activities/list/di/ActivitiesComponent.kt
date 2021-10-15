package com.skillbox.ascentstrava.presentation.activities.list.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.activities.list.ActivitiesFragment
import com.skillbox.ascentstrava.presentation.activities.create.CreateActivityFragment
import dagger.Component

@ActivitiesScope
@Component(
    dependencies = [AppComponent::class]
)
interface ActivitiesComponent {

    fun inject(activitiesFragment: ActivitiesFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ActivitiesComponent
    }
}