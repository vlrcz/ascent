package com.skillbox.ascentstrava.data

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator @Inject constructor(
    private val authManager: AuthManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        /*val originalRequest = response.request
        if (originalRequest.header("Authorization") != null) return null

        return runBlocking {
            if (authManager.receiveAccessToken()?.isEmpty() == true) {
                originalRequest.newBuilder()
                    .header(
                        AuthConfig.AUTHORIZATION_HEADER,
                        "token ${authManager.receiveAccessToken()}"
                    )
                    .build()
            } else null
        }*/
        return response.request
    }

    companion object {
        private const val UNAUTHORIZED_CODE = 401
    }
}
