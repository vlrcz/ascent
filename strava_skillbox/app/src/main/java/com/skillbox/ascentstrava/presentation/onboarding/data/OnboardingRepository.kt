package com.skillbox.ascentstrava.presentation.onboarding.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class OnboardingRepository @Inject constructor(private val context: Context) {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    fun addFlagAfterFirstEntry() {
        sharedPrefs.edit()
            .putBoolean(FIRST_ENTRY, true)
            .commit()
    }

    fun isFirstEntry(): Boolean {
        return sharedPrefs.contains(FIRST_ENTRY)
    }

    companion object {
        private const val FIRST_ENTRY = "first_entry"
    }
}