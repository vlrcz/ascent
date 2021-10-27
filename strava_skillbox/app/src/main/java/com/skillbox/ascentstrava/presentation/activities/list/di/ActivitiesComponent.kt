package com.skillbox.ascentstrava.presentation.activities.list.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.activities.list.ActivityListFragment
import dagger.Component

@ActivitiesScope
@Component(
    dependencies = [AppComponent::class]
)
interface ActivitiesComponent {

    fun inject(activityListFragment: ActivityListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ActivitiesComponent
    }
}