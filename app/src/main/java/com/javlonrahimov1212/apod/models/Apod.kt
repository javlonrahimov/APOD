package com.javlonrahimov1212.apod.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["url"], unique = true)])
data class Apod(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(defaultValue = "NASA") var copyright: String?,
    @ColumnInfo val date: String,
    @ColumnInfo val explanation: String,
    @ColumnInfo @SerializedName("hdurl")
    val hdUrl: String?,
    @ColumnInfo val media_type: String,
    @ColumnInfo val service_version: String,
    @ColumnInfo val title: String,
    @ColumnInfo(name = "url") val url: String
)