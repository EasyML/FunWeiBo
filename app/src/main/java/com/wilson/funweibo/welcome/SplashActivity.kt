package com.wilson.funweibo.welcome

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wilson.funweibo.R
import com.wilson.funweibo.home.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var loadingView: SplashLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wb_splash)
        loadingView = findViewById(R.id.splash_loading)
        loadingView.setListener {
            startMainActivity()
        }
        loadingView.setOnClickListener {
            startMainActivity()
        }
        loadingView.startProgress()

    }

    private fun startMainActivity(){
        loadingView.stopProgress()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
