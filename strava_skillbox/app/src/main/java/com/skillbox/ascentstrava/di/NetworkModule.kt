package com.skillbox.ascentstrava.di

import com.skillbox.ascentstrava.data.CustomHeaderInterceptor
import com.skillbox.ascentstrava.data.StravaApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
abstract class NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun providesOkHttpClient(
            @HeaderInterceptor
            headerInterceptor: Interceptor,
            @LoggingInterceptor
            loggingInterceptor: Interceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .followRedirects(true)
                .build()
        }

        @LoggingInterceptor
        @Provides
        fun provideLoggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @Provides
        @Singleton
        fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://developers.strava.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Provides
        @Singleton
        fun providesApi(retrofit: Retrofit): StravaApi {
            return retrofit.create()
        }
    }

    @HeaderInterceptor
    @Binds
    abstract fun providerHeaderInterceptor(
        customHeaderInterceptor: CustomHeaderInterceptor
    ): Interceptor
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeaderInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor