package com.example.eventfinder.Activity

import com.example.eventfinder.Client.UserApi
import com.example.eventfinder.Model.LoginRequest
import com.example.eventfinder.Model.LoginResponse
import retrofit2.Response
import retrofit2.http.QueryMap

class UserRepository {

    suspend fun loginUser(@QueryMap params: Map<String, String?>?): Response<LoginResponse>? {
        return  UserApi.getApi()?.loginUser(params = params)
    }

    suspend fun registerUser(@QueryMap params: Map<String, String?>?): Response<LoginResponse>? {
        return  UserApi.getApi()?.registerUser(params = params)
    }

}