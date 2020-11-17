package com.javlonrahimov1212.apod.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.javlonrahimov1212.apod.models.Apod

@Dao
interface ApodDao {

    @Query("INSERT INTO Apod (copyright, date, explanation, hdurl, media_type, service_version, title, url, isLiked) VALUES(:copyright,:date,:explanation,:hdUrl,:media_type, :service_version, :title, :url, :is_liked)")
    suspend fun insertApod(
        copyright: String,
        date: String,
        explanation: String,
        hdUrl: String,
        media_type: String,
        service_version: String,
        title: String,
        url: String,
        is_liked: Boolean
    )

    @Transaction
    suspend fun insertApods(apods: List<Apod>) {
        for (i in apods) {
            insertApod(
                copyright = i.copyright,
                date = i.date,
                explanation = i.explanation,
                hdUrl = i.hdUrl,
                media_type = i.media_type,
                service_version = i.service_version,
                title = i.title,
                url = i.url,
                is_liked = i.isLiked
            )
        }
    }

    @Delete
    suspend fun deleteApod(apod: Apod)

    @Update
    suspend fun updateApod(apod: Apod)

    @Query("SELECT * FROM apod")
    fun getAllApods(): LiveData<List<Apod>>

    @Query("SELECT * FROM apod ORDER BY date DESC LIMIT 1")
    fun getNewestApod(): LiveData<Apod>

    @Query("SELECT * FROM apod ORDER BY date DESC LIMIT 30")
    fun getLast30Apods(): LiveData<List<Apod>>

    @Query("SELECT * FROM apod WHERE date = :date")
    fun getApodByDate(date: String): LiveData<Apod>

    @Query("SELECT EXISTS (SELECT * FROM apod WHERE date = :date)")
    suspend fun exists(date: String): Boolean

    @Query("SELECT * FROM apod  WHERE isLiked == :bool ORDER BY date DESC")
    fun getFavouriteApods(bool: Boolean) : LiveData<List<Apod>>
}