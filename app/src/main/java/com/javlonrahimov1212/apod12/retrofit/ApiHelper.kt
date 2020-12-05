package com.javlonrahimov1212.apod12.retrofit

import com.javlonrahimov1212.apod12.models.Apod

class ApiHelper(private val apiService: ApiService) {
    suspend fun getApodToday() = apiService.getApodToday()

    suspend fun getLast10Apod(days: List<String>): List<Apod> {
        val result = ArrayList<Apod>()
        for (i in days) {
            result.add(apiService.getApodByDate(i))
        }
        return result
    }

    suspend fun getApodByDate(date: String) = apiService.getApodByDate(date)
}