package com.example.shopkart.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.shopkart.R
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.DashboardActivity
import com.example.shopkart.ui.activities.base.BaseActivity
import com.example.shopkart.ui.activities.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var firebaseUtil: FirebaseUtil

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(3000)
            firebaseUtil.firebaseAuth.currentUser?.let {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                finish()
                return@launch
            }
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}