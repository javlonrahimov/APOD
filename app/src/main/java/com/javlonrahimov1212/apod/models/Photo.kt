package com.javlonrahimov1212.apod.models

import java.io.Serializable

data class Photo(
    val title: String,
    val description: String,
    val url: String,
    val dateCreated: String
) : Serializable