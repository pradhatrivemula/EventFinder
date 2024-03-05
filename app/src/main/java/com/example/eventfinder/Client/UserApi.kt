package com.example.eventfinder.Client

import com.example.eventfinder.Model.LoginRequest
import com.example.eventfinder.Model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface UserApi {

    @GET("/login")
    suspend fun loginUser(@QueryMap params: Map<String, String?>?, ): Response<LoginResponse>

    @GET("/signup")
    suspend fun registerUser(@QueryMap params: Map<String, String?>?): Response<LoginResponse>

    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}