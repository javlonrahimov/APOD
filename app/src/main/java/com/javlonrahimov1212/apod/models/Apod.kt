package com.javlonrahimov1212.apod.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index(value = ["url"], unique = true)])
data class Apod(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(defaultValue = "NASA")
    var copyright: String = "",
    @ColumnInfo
    val date: String = "",
    @ColumnInfo
    val explanation: String = "",
    @ColumnInfo(defaultValue = "")
    @SerializedName("hdurl")
    val hdUrl: String = "",
    @ColumnInfo
    val media_type: String = "",
    @ColumnInfo
    val service_version: String = "",
    @ColumnInfo
    val title: String = "",
    @ColumnInfo(name = "url")
    val url: String = "",
    @ColumnInfo(defaultValue = false.toString())
    var isLiked: Boolean = false
) : Serializable {
    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass)
            return false

        other as Apod

        if (id != other.id)
            return false

        if (copyright != other.copyright)
            return false

        if (date != other.date)
            return false

        if (explanation != other.explanation)
            return false

        if (hdUrl != other.hdUrl)
            return false

        if (media_type != other.media_type)
            return false

        if (service_version != other.service_version)
            return false

        if (url != other.url)
            return false

        if (isLiked != other.isLiked)
            return false

        return true
    }
}