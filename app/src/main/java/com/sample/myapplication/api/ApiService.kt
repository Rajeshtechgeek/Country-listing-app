package com.sample.myapplication.api

import com.sample.myapplication.BuildConfig
import com.sample.myapplication.api.response.CountryResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Used to connect to the Country API to fetch countries
 */
interface ApiService {

    @GET("rest/v2/all ")
    suspend fun getCountries(): CountryResponse

    companion object {
        private var instance: ApiService? = null

        /**
         * This  method returns single instance of [ApiService]
         *
         * @return [ApiService] instance
         */
        fun getInstance(): ApiService {
            if (instance == null) { // Single Checked
                synchronized(ApiService::class.java) {
                    if (instance == null) { // Double checked
                        instance = create()
                    }
                }
            }
            return instance!!
        }

        private fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
