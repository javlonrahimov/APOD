package com.javlonrahimov1212.apod.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.javlonrahimov1212.apod.models.Apod

@Database(entities = [Apod::class], version = 1, exportSchema = false)
abstract class ApodDatabase : RoomDatabase() {

    abstract fun apodDao(): ApodDao

    companion object {
        @Volatile
        private var INSTANCE: ApodDatabase? = null

        fun getDatabase(context: Context): ApodDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApodDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}