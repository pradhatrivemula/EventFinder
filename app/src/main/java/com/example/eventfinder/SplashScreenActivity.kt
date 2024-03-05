package com.example.eventfinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eventfinder.Activity.LoginActivity

class SplashScreenActivity constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        finish()
    }
}