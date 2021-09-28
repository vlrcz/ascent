package com.skillbox.ascentstrava.di.module

import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.data.AuthInterceptor
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.data.StravaApi
import com.skillbox.ascentstrava.data.TokenAuthenticator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton
import okhttp3.Authenticator

@Module
abstract class NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun providesOkHttpClient(
            interceptors: Set<Interceptor>,
            tokenAuthenticator: TokenAuthenticator
        ): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
            interceptors.forEach {
                okHttpClient.addInterceptor(it)
            }
            return okHttpClient
                .authenticator(tokenAuthenticator)
                .followRedirects(true)
                .build()
        }

        @Provides
        @IntoSet
        fun provideLoggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @Provides
        @Singleton
        fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(AuthConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Provides
        @Singleton
        fun providesApi(retrofit: Retrofit): StravaApi {
            return retrofit.create()
        }

        @Provides
        @Singleton
        fun provideTokenAuthenticator(
            stravaApi: StravaApi,
            authManager: AuthManager
        ): Authenticator {
            return TokenAuthenticator(stravaApi, authManager)
        }
    }

    @Binds
    @IntoSet
    abstract fun providerHeaderInterceptor(
        authInterceptor: AuthInterceptor
    ): Interceptor
}
