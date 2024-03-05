package com.example.eventfinder.Client

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient constructor() {
    val apIs: APIs

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                val url = request.url.toString()
                Log.d("Retrofit URL", url)
                chain.proceed(request)
            })
            .build()

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apIs = retrofit.create(APIs::class.java)
    }

    companion object {
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }
            return instance!!
        }
    }
}
