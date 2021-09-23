package com.skillbox.ascentstrava.di

import android.app.Application
import com.skillbox.ascentstrava.presentation.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ContextModule::class, RepositoryModule::class]
)
interface AppComponent {

    fun inject(mainFragment: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}