package com.skillbox.ascentstrava.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun observerAuth(): Flow<Boolean> = authListener

    fun login(tokenResponse: TokenResponse) {
        sharedPrefs.edit()
            .putString(ACCESS_TOKEN, tokenResponse.accessToken)
            .putString(REFRESH_TOKEN, tokenResponse.refreshToken)
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

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val REFRESH_TOKEN = "Refresh Token"
    }
}
