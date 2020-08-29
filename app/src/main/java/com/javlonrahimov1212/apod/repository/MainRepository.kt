package com.javlonrahimov1212.apod.repository

import com.javlonrahimov1212.apod.retrofit.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getApodToday() = apiHelper.getApodToday()
    suspend fun getLAst30Apods(days: List<String>) = apiHelper.getLast30Apod(days)
}