package com.skillbox.ascentstrava.presentation.profile.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteManager @Inject constructor() {

    private var profileUrl: String? = null

    fun getProfileUrl(): String? {
        return profileUrl
    }

    fun clearProfileUrl() {
        profileUrl = null
    } // todo добавить в кнопку logout

    fun putProfileUrl(url: String) {
        profileUrl = url
    }
}