package com.example.eventfinder.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("message")
    var message: String
)