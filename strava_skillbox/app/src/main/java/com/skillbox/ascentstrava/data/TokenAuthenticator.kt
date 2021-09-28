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
            if (response.code == UNAUTHORIZED_CODE) {
                val refreshToken = authManager.receiveRefreshToken()
                val tokenResponse = stravaApi.refreshAccessToken(refreshToken)
                tokenResponse.accessToken?.let { authManager.saveAccessToken(it) }
                tokenResponse.refreshToken?.let { authManager.saveRefreshToken(it) }
                response.request.newBuilder()
                    .addHeader(
                        AuthConfig.AUTHORIZATION_HEADER,
                        "token ${authManager.receiveAccessToken()}"
                    )
                    .build()
            } else null
        }
    }

    companion object {
        private const val UNAUTHORIZED_CODE = 401
    }
}