package com.ajiashi.imagetotextconverter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class ActivityWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val handler = Handler(Looper.getMainLooper())
        val myRunnable = Runnable {
            val intent = Intent(this@ActivityWelcome,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        handler.postDelayed(myRunnable, 2000)

    }
}