package com.example.shopkart.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.example.shopkart.R
import com.example.shopkart.ui.activities.BaseActivity
import com.example.shopkart.ui.activities.login.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}