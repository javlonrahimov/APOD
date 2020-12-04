package com.javlonrahimov1212.apod.repository

import android.icu.text.SimpleDateFormat
import com.javlonrahimov1212.apod.database.ApodDao
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import java.util.*

class MainRepository(private val apiHelper: ApiHelper, private val apodDao: ApodDao) {

    fun getApodToday() = apodDao.getNewestApod()
    fun getLast30Apods() = apodDao.getLast30Apods()
    fun getApodByDate(date: String) = apodDao.getApodByDate(date)
    fun getFavouritesApod() = apodDao.getFavouriteApods(true)

    suspend fun setApodToday(): Boolean {
        if (!apodDao.exists(getCurrentDate())) {
            var apod = apiHelper.getApodToday()
            if (apod.copyright == "")
                apod = apod.copy(copyright = "NASA")
            apodDao.insertApod(apod)
            return true
        }
        return false
    }

    suspend fun setLast10Apods(days: List<String>) {
        val apods = apiHelper.getLast10Apod(days)
        for (i in apods.indices) {
            if (apods[i].copyright == "")
                (apods as ArrayList<Apod>)[i] = apods[i].copy(copyright = "NASA")
        }
        apodDao.insertApods(apods)
    }

    suspend fun setApodByDate(date: String) {
        if (!apodDao.exists(date)) {
            val apod = apiHelper.getApodByDate(date)
            apodDao.insertApod(apod)
        }
    }

    suspend fun updateApod(apod: Apod) {
        apodDao.updateApod(apod)
    }

    private fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return simpleDateFormat.format(Date())
    }
}