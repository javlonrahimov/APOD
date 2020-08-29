package com.javlonrahimov1212.apod.retrofit

import com.javlonrahimov1212.apod.models.Apod
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("apod?api_key=rqhp0j8ebgEyieuJgOTvMbyUmqc3VHoFzqiGWDFG")
    suspend fun getApodToday(): Apod

    @GET("apod?api_key=rqhp0j8ebgEyieuJgOTvMbyUmqc3VHoFzqiGWDFG")
    suspend fun getApodByDate(@Query("date") date: String): Apod
}