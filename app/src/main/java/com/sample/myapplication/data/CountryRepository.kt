package com.sample.myapplication.data

import com.sample.myapplication.api.ApiService
import com.sample.myapplication.api.WeatherApiService
import com.sample.myapplication.api.country.CountryResponseItem
import com.sample.myapplication.api.weather.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository private constructor(
    private val apiService: ApiService,
    private val weatherApiService: WeatherApiService,
) {

    /**
     * local cache for the list
     */
    var countryList: List<CountryResponseItem> = ArrayList()

    suspend fun getCountries(): List<CountryResponseItem> {
        return withContext(Dispatchers.IO) {
            countryList = apiService.getCountries()
            return@withContext countryList
        }
    }

    suspend fun getWeather(lat: String, lon: String, apiKey: String, units: String):
            WeatherResponse {
        return withContext(Dispatchers.IO) {
            return@withContext weatherApiService.getWeatherCity(lat, lon, apiKey, units)
        }
    }

    companion object {
        private var instance: CountryRepository? = null

        /**
         * This  method returns single instance of [CountryRepository]
         *
         * @return [CountryRepository] instance
         */
        fun getInstance(
            apiService: ApiService,
            weatherApiService: WeatherApiService
        ): CountryRepository {
            if (instance == null) { // Single Checked
                synchronized(ApiService::class.java) {
                    if (instance == null) { // Double checked
                        instance = CountryRepository(apiService, weatherApiService)
                    }
                }
            }
            return instance!!
        }
    }
}
