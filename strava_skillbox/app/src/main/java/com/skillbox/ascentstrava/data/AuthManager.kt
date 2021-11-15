package com.skillbox.ascentstrava.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val sharedPrefs: SharedPreferences) {

    private val authListener = MutableStateFlow(false)

    init {
        val accessToken = fetchAccessToken()
        val refreshToken = fetchRefreshToken()

        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            authListener.value = true
        }
    }

    fun observeAuth(): StateFlow<Boolean> = authListener.asStateFlow()

    fun login(accessToken: String, refreshToken: String) {
        sharedPrefs.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()

        authListener.value = true
    }

    fun logout() {
        sharedPrefs.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .apply()

        authListener.value = false
    }

    fun fetchAccessToken(): String? {
        return sharedPrefs.getString(ACCESS_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return sharedPrefs.getString(REFRESH_TOKEN, null)
    }

    fun saveLocale(language: String) {
        sharedPrefs.edit()
            .putString(LOCALE, language)
            .apply()
    }

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val REFRESH_TOKEN = "Refresh Token"
        private const val LOCALE = "Locale"
    }
}
