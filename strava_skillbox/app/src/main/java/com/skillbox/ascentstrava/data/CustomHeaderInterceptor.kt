package com.skillbox.ascentstrava.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CustomHeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "token ${AuthToken.accessToken}")
            .build()
        return chain.proceed(modifiedRequest)
    }
}