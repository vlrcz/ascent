package com.skillbox.ascentstrava.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val sharedPrefs: SharedPreferences) {

    fun saveAccessToken(accessToken: String) {
        sharedPrefs.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPrefs.edit()
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun receiveAccessToken(): String? {
        return sharedPrefs.getString(ACCESS_TOKEN, null)
    }

    fun receiveRefreshToken(): String? {
        return sharedPrefs.getString(REFRESH_TOKEN, null)
    }

    fun brokeAccessToken() {
        saveAccessToken("a5syuy67") //todo удалить
    }

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val REFRESH_TOKEN = "Refresh Token"
    }
}
