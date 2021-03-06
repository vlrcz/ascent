package com.skillbox.ascentstrava.presentation.main.data

import android.content.SharedPreferences
import javax.inject.Inject

class MainRepository @Inject constructor(private val sharedPrefs: SharedPreferences) {

    fun addFlagAfterFirstEntry() {
        sharedPrefs.edit()
            .putBoolean(FIRST_ENTRY, true)
            .apply()
    }

    fun isFirstEntry(): Boolean {
        return sharedPrefs.contains(FIRST_ENTRY)
    }

    fun containsAccessToken(): Boolean {
        return sharedPrefs.contains(ACCESS_TOKEN)
    }

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val FIRST_ENTRY = "first_entry"
    }
}