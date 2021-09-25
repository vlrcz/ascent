package com.skillbox.ascentstrava.data

object AuthToken {

    lateinit var accessToken: String

    fun setToken(accessToken: String) {
        this.accessToken = accessToken
    }
}