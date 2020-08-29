package com.javlonrahimov1212.apod.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.javlonrahimov1212.apod.models.Apod

@Dao
interface ApodDao {

    @Insert
    fun insertApod(vararg apods: Apod)

    @Delete
    fun deleteApod(apod: Apod)

    @Query("SELECT * FROM apod")
    fun getAllApods(): LiveData<List<Apod>>

    @Query("SELECT * FROM apod LIMIT 1")
    fun getNewestApods(): LiveData<Apod>
}