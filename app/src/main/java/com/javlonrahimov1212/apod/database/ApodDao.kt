package com.javlonrahimov1212.apod.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.javlonrahimov1212.apod.models.Apod

@Dao
interface ApodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertApod(apod: Apod)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApods(apods: List<Apod>)

    @Delete
    suspend fun deleteApod(apod: Apod)

    @Query("SELECT * FROM apod")
    fun getAllApods(): LiveData<List<Apod>>

    @Query("SELECT * FROM apod ORDER BY date DESC LIMIT 1")
    fun getNewestApod(): LiveData<Apod>

    @Query("SELECT * FROM apod ORDER BY date DESC LIMIT 30")
    fun getLast30Apods(): LiveData<List<Apod>>

    @Query("SELECT * FROM apod WHERE date = :date")
    fun getApodByDate(date: String): LiveData<Apod>
}