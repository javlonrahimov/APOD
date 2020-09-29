package com.javlonrahimov1212.apod.retrofit

import com.javlonrahimov1212.apod.models.Apod

class ApiHelper(private val apiService: ApiService) {
    suspend fun getApodToday() = apiService.getApodToday()

    suspend fun getLast30Apod(days: List<String>): List<Apod> {
        val result = ArrayList<Apod>()
        for (i in days) {
            result.add(apiService.getApodByDate(i))
        }
        return result
    }

    suspend fun getApodByDate(date: String) = apiService.getApodByDate(date)

    suspend fun getSearchResults(query: String) = apiService.getSearchResults(query, "image")
}