package com.skillbox.ascentstrava.di

import com.skillbox.ascentstrava.storage.StorageRepositoryImpl
import com.skillbox.ascentstrava.storage.StorageViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun providesStorageRepository(impl: StorageRepositoryImpl): StorageRepository

    companion object {
        @Provides
        fun getStorageViewModel(impl: StorageRepositoryImpl): StorageViewModel {
            return StorageViewModel(impl)
        }
    }
}