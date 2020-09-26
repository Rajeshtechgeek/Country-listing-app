package com.sample.myapplication.ui.main

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.myapplication.api.ApiService
import com.sample.myapplication.api.response.CountryResponseItem
import com.sample.myapplication.data.CountryRepository
import com.sample.myapplication.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class MainViewModel : ViewModel() {

    private var searchJob: Job? = null
    private val repository: CountryRepository =
        CountryRepository.getInstance(ApiService.getInstance())

    private val _countryList = MutableLiveData<List<CountryResponseItem>>()

    val countryList: LiveData<List<CountryResponseItem>>
        get() = _countryList

    /**
     * local cache for the list
     */
    var countryListCopy: List<CountryResponseItem> = ArrayList()

    /**
     * temporary list for filtering
     */
    val temp = ArrayList<CountryResponseItem>()

    fun getCountryList(forceRefresh: Boolean) {
        Log.d(TAG, "getCountryList force refresh: $forceRefresh")
        viewModelScope.launch {
            if (countryListCopy.isEmpty() || forceRefresh) {
                countryListCopy = repository.getCountries()
                _countryList.value = countryListCopy
            } else {
                _countryList.value = countryListCopy
            }
        }
    }

    fun filter(query: String?) {
        Log.d(TAG, "search string: $query")
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            // do filter in background thread and post value in main thread
            _countryList.value = doFilter(query)
        }
    }

    private suspend fun doFilter(query: String?): List<CountryResponseItem> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "filterRecentChatList() is working in thread ${Thread.currentThread().name}")
            temp.clear()
            if (!TextUtils.isEmpty(query)) {
                for (s in countryListCopy) {
                    if (s.name!!.toLowerCase(Locale.getDefault()).contains(query!!)) {
                        temp.add(s)
                    }
                }
            } else {
                temp.addAll(countryListCopy)
            }
            return@withContext temp
        }
}