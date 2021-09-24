package com.skillbox.ascentstrava.storage

import android.content.Context
import javax.inject.Inject

class StorageRepository @Inject constructor(private val context: Context) {

    private val sharedPrefs by lazy {
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addFlagAfterFirstEntry() {
        sharedPrefs.edit()
            .putString(FIRST_ENTRY, "first_time_entry")
            .commit()
    }

    fun isFirstEntry(): Boolean {
        return sharedPrefs.contains(FIRST_ENTRY)
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs"
        private const val FIRST_ENTRY = "first_entry"
    }
}