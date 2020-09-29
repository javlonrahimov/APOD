package com.javlonrahimov1212.apod.retrofit

import com.google.gson.JsonObject
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.models.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("apod?api_key=rqhp0j8ebgEyieuJgOTvMbyUmqc3VHoFzqiGWDFG")
    suspend fun getApodToday(): Apod

    @GET("apod?api_key=rqhp0j8ebgEyieuJgOTvMbyUmqc3VHoFzqiGWDFG")
    suspend fun getApodByDate(@Query("date") date: String): Apod

    @GET("search")
    suspend fun getSearchResults(
        @Query("q") q: String,
        @Query("media_type") mediaType: String
    ): SearchResult
}