package com.drivemap.app

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

class DriveMapApp : Application() {
    lateinit var http: OkHttpClient
    lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        http = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true; isLenient = true }

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.example/") // TODO: ganti ke base URL Edge Functions
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(http)
            .build()
    }
}
