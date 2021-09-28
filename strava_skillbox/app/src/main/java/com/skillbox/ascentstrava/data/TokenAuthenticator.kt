package com.skillbox.ascentstrava.data

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator @Inject constructor(
    private val stravaApi: StravaApi,
    private val authManager: AuthManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = authManager.refreshToken
            val tokenResponse = stravaApi.refreshAccessToken(refreshToken)
            tokenResponse.access_token?.let { authManager.saveAccessToken(it) }
            tokenResponse.refresh_token?.let { authManager.saveRefreshToken(it) }
            response.request.newBuilder()
                .addHeader(AuthConfig.AUTHORIZATION_HEADER, "token ${authManager.accessToken}")
                .build()
        }
    }
}