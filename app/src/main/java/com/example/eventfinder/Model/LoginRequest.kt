package com.example.eventfinder.Model

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    var email: String,

    var pass: String
)