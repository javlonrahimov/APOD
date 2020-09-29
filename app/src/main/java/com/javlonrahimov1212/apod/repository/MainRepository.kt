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

    suspend fun setApodToday() {
        if (!apodDao.exists(getCurrentDate())) {
            val apod = apiHelper.getApodToday()
            apodDao.insertApod(
                copyright = apod.copyright,
                date = apod.date,
                explanation = apod.explanation,
                hdUrl = apod.hdUrl,
                media_type = apod.media_type,
                service_version = apod.service_version,
                title = apod.title,
                url = apod.url,
                is_liked = apod.isLiked
            )
        }
    }

    suspend fun setLast30Apods(days: List<String>) {
        val apods = apiHelper.getLast30Apod(days)
        apodDao.insertApods(apods)
    }

    suspend fun setApodByDate(date: String) {
        if (!apodDao.exists(date)) {
            val apod = apiHelper.getApodByDate(date)
            apodDao.insertApod(
                copyright = apod.copyright,
                date = apod.date,
                explanation = apod.explanation,
                hdUrl = apod.hdUrl,
                media_type = apod.media_type,
                service_version = apod.service_version,
                title = apod.title,
                url = apod.url,
                is_liked = apod.isLiked
            )
        }
    }

    suspend fun getSearchResults(query: String) = apiHelper.getSearchResults(query)

    suspend fun updateApod(apod: Apod){
        apodDao.updateApod(apod)
    }

    private fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return simpleDateFormat.format(Date())
    }
}