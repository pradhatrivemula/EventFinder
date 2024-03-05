package com.example.eventfinder.Client

import com.example.eventfinder.Model.Event
import com.example.eventfinder.Model.EventDetailsModel
import com.example.eventfinder.Model.LoginRequest
import com.example.eventfinder.Model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface APIs {
    @GET("newShowEventTable")
    fun getEventsTable(@QueryMap params: Map<String, String?>?): Call<List<Event>>

    @GET("newShowEventDetails")
    fun getEventsDetailsTable(@QueryMap params: Map<String, String?>?): Call<EventDetailsModel>

    companion object {
        const val BASE_URL = "https://assignment8-382521.wl.r.appspot.com"
    }
}