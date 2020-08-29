package com.javlonrahimov1212.apod.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Apod(
    @ColumnInfo val copyright: String = "NASA",
    @ColumnInfo val date: String,
    @ColumnInfo val explanation: String,
    @ColumnInfo @SerializedName("hdurl")
    val hdUrl: String,
    @ColumnInfo val media_type: String,
    @ColumnInfo val service_version: String,
    @ColumnInfo val title: String,
    @ColumnInfo val url: String
)