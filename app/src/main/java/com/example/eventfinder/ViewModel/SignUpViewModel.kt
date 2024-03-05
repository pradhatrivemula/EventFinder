package com.example.eventfinder.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventfinder.Activity.UserRepository
import com.example.eventfinder.Model.BaseResponse
import com.example.eventfinder.Model.LoginRequest
import com.example.eventfinder.Model.LoginResponse
import kotlinx.coroutines.launch
import java.util.HashMap

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    fun loginUser(email: String, pwd: String) {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val data: MutableMap<String, String?> = HashMap()
                data.put("email", email)
                data.put("pass", pwd)

                val response = userRepo.loginUser(params = data)
                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                    Log.d("User", "User Logged In")
                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                    Log.d("Error", response?.message().toString())
                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
                Log.d("Error", ex.message.toString())
            }
        }
    }
}