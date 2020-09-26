package com.sample.myapplication.api

import com.sample.myapplication.BuildConfig
import com.sample.myapplication.api.country.CountryResponse
import com.sample.myapplication.api.weather.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Used to connect to the Country API to fetch countries
 */
interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherCity(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String?,
        @Query("units") units: String?,
    ): WeatherResponse

    companion object {
        private var instance: WeatherApiService? = null

        /**
         * This  method returns single instance of [WeatherApiService]
         *
         * @return [WeatherApiService] instance
         */
        fun getInstance(): WeatherApiService {
            if (instance == null) { // Single Checked
                synchronized(WeatherApiService::class.java) {
                    if (instance == null) { // Double checked
                        instance = create()
                    }
                }
            }
            return instance!!
        }

        private fun create(): WeatherApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}
