package com.skillbox.ascentstrava.presentation.share.di

import com.skillbox.ascentstrava.di.AppComponent
import com.skillbox.ascentstrava.presentation.share.ShareFragment
import dagger.Component

@ShareScope
@Component(
    dependencies = [AppComponent::class]
)
interface ShareComponent {

    fun inject(shareFragment: ShareFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ShareComponent
    }
}