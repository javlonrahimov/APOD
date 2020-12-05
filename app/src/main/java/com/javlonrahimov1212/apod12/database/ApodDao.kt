package com.javlonrahimov1212.apod12.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.javlonrahimov1212.apod12.models.Apod

@Dao
interface ApodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApod(apod: Apod)

    @Transaction
    suspend fun insertApods(apods: List<Apod>) {
        for (i in apods) {
            insertApod(i)
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
    fun getFavouriteApods(bool: Boolean): LiveData<List<Apod>>
}