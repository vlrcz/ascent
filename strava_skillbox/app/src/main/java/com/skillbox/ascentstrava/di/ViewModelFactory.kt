package com.skillbox.ascentstrava.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skillbox.ascentstrava.storage.StorageRepository
import com.skillbox.ascentstrava.storage.StorageViewModel

class ViewModelFactory<T : ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return create.invoke() as T
    }
}