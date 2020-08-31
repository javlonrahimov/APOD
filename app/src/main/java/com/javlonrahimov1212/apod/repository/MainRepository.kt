package com.javlonrahimov1212.apod.repository

import com.javlonrahimov1212.apod.database.ApodDao
import com.javlonrahimov1212.apod.retrofit.ApiHelper

class MainRepository(private val apiHelper: ApiHelper, private val apodDao: ApodDao) {

    fun getApodToday() = apodDao.getNewestApod()
    fun getLast30Apods() = apodDao.getLast30Apods()

    suspend fun setApodToday() {
        val apod = apiHelper.getApodToday()
        apodDao.insertApod(apod)
    }

    suspend fun setLast30Apods(days: List<String>) {
        val apods = apiHelper.getLast30Apod(days)
        apodDao.insertApods(apods)
    }
}