package com.skillbox.ascentstrava.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() {

    var accessToken: String? = null
    var refreshToken: String? = null

    fun saveAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun saveRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}