package com.example.eventfinder.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.eventfinder.MainActivity
import com.example.eventfinder.Model.BaseResponse
import com.example.eventfinder.Model.LoginResponse
import com.example.eventfinder.R
import com.example.eventfinder.Util.SessionManager
import com.example.eventfinder.ViewModel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var mChangeLang: Button
    private lateinit var signupBtn: TextView
    private lateinit var emailEdiText: TextInputEditText
    private lateinit var passwordEdiText: TextInputEditText
    private lateinit var prgbar: ProgressBar

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        setContentView(R.layout.activity_login)

        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.app_name)

        loginBtn = findViewById(R.id.btn_login)
        mChangeLang = findViewById(R.id.mChangeLang)
        signupBtn = findViewById(R.id.signUp)
        emailEdiText = findViewById(R.id.txtInput_email)
        passwordEdiText = findViewById(R.id.txt_pass)
        prgbar = findViewById(R.id.prgbar)

        mChangeLang.setOnClickListener{
            showChangeLang()
        }

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }

        viewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

        loginBtn.setOnClickListener {
            doLogin()

        }

        signupBtn.setOnClickListener {
            doSignup()
        }
    }

    private fun showChangeLang() {
        val listItems = arrayOf("English", "española", "हिंदी")

        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setTitle("Choose Language")
        builder.setSingleChoiceItems(listItems, -1) {dialog, which ->
            when (which) {
                0 -> {
                    setLocale("en")
                    recreate()
                }
                1 -> {
                    setLocale("es")
                    recreate()
                }
                2 -> {
                    setLocale("hi")
                    recreate()
                }
            }

            dialog.dismiss()
        }
        val mDialog = builder.create()
        mDialog.show()
    }

    private fun setLocale(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)


        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocale() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocale(language.toString())
    }

    fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun doLogin() {
        val email = emailEdiText.text.toString()
        val pwd = passwordEdiText.text.toString()
        viewModel.loginUser(email = email, pwd = pwd)

    }

    fun doSignup() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun showLoading() {
        prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        prgbar.visibility = View.GONE
    }

    fun processLogin(data: LoginResponse?) {
        showToast("Success:" + data?.message)
        Log.d("Success:", data?.message.toString())
        if (!data?.message.isNullOrEmpty()) {
            data?.message?.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }
    }

    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}