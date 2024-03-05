package com.example.eventfinder.Model

import com.google.gson.annotations.SerializedName

data class SignInResponse(

    @SerializedName("message")
    var message: String
)