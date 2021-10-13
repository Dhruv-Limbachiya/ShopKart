package com.example.shopkart.ui.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.shopkart.R
import com.example.shopkart.databinding.ActivityLoginBinding
import com.example.shopkart.ui.activities.BaseActivity
import com.example.shopkart.ui.activities.registration.RegistrationActivity

class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        mBinding.tvDontHaveAnAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }
}