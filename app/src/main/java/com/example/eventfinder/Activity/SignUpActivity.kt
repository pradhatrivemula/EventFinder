package com.example.eventfinder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.eventfinder.Model.BaseResponse
import com.example.eventfinder.Model.LoginResponse
import com.example.eventfinder.R
import com.example.eventfinder.Util.SessionManager
import com.example.eventfinder.ViewModel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupBtn: Button
    private lateinit var loginBtn: TextView
    private lateinit var emailEdiText: TextInputEditText
    private lateinit var passwordEdiText: TextInputEditText
    private lateinit var prgbar: ProgressBar

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        loginBtn = findViewById(R.id.login_txt)
        signupBtn = findViewById(R.id.btnSign)
        emailEdiText = findViewById(R.id.signup_txt_email)
        passwordEdiText = findViewById(R.id.sign_txt_pass)
        prgbar = findViewById(R.id.signup_prgbar)

        viewModel.signInResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin()
                }

                is BaseResponse.Error -> {
                    stopLoading()
                    processLogin()
                }
                else -> {
                    stopLoading()
                }
            }
        }

        signupBtn.setOnClickListener {
            doSignup()
        }

        loginBtn.setOnClickListener {
            doLogin()
        }
    }

    fun doLogin() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun doSignup() {
        val email = emailEdiText.text.toString()
        val pwd = passwordEdiText.text.toString()
        viewModel.registerUser(email = email, pwd = pwd)
    }

    fun showLoading() {
        prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        prgbar.visibility = View.GONE
    }

    fun processLogin() {
        showToast("User Created Successfully")
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}