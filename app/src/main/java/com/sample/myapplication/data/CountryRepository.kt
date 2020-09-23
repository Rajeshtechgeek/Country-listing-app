package com.sample.myapplication.data

import com.sample.myapplication.api.ApiService
import com.sample.myapplication.api.response.CountryResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository private constructor(private val apiService: ApiService) {

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

    fun getCountryDetail(position: Int): CountryResponseItem {
        return countryList.get(position)
    }

    companion object {
        private var instance: CountryRepository? = null

        /**
         * This  method returns single instance of [CountryRepository]
         *
         * @return [CountryRepository] instance
         */
        fun getInstance(apiService: ApiService): CountryRepository {
            if (instance == null) { // Single Checked
                synchronized(ApiService::class.java) {
                    if (instance == null) { // Double checked
                        instance = CountryRepository(apiService)
                    }
                }
            }
            return instance!!
        }
    }
}
