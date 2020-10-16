package com.farazrizki13.rentalcar2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val login = sharedPreferences.getBoolean("IS_LOGIN", false)

        val handler = Handler()
        handler.postDelayed({
            if (login) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        },3000)
    }
}