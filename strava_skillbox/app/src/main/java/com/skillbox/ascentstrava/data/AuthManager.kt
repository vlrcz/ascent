package com.skillbox.ascentstrava.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() {

    var accessToken: String? = null

    fun saveToken(accessToken: String) {
        this.accessToken = accessToken
    }
}