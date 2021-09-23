package com.skillbox.ascentstrava.storage

import android.content.Context
import com.skillbox.ascentstrava.di.StorageRepository
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(private val context: Context) : StorageRepository {

    private val sharedPrefs by lazy {
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun addFlagAfterFirstEntry() {
        sharedPrefs.edit()
            .putString(FIRST_ENTRY, "first_time_entry")
            .commit()
    }

    override fun isFirstEntry(): Boolean {
        return sharedPrefs.contains(FIRST_ENTRY)
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs"
        private const val FIRST_ENTRY = "first_entry"
    }
}