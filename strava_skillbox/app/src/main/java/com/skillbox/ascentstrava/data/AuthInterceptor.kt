package com.skillbox.ascentstrava.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val authManager: AuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = authManager.receiveAccessToken()
        if (accessToken != null) {
            val modifiedRequest = originalRequest.newBuilder()
                .addHeader(AuthConfig.AUTHORIZATION_HEADER, "token $accessToken}")
                .build()
            return chain.proceed(modifiedRequest)
        }
        return chain.proceed(originalRequest)
    }
}