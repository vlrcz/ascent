package com.skillbox.ascentstrava.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    const val AUTH_URI = "https://www.strava.com/oauth/authorize"
    const val TOKEN_URI = "https://www.strava.com/oauth/token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "activity:write,read"

    const val CLIENT_ID = "71710"
    const val CLIENT_SECRET = "b84830ac3f06531c0f2f8ce41a7670e6ad27f876"
    const val CALLBACK_URL = "ascent://ru.ascent.strava"

    const val AUTHORIZATION = "Authorization"
    const val BASE_URL = "https://developers.strava.com"
}