package com.example.shopkart.ui.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.shopkart.R
import com.example.shopkart.databinding.ActivityLoginBinding
import com.example.shopkart.ui.activities.DashboardActivity
import com.example.shopkart.ui.activities.base.BaseActivity
import com.example.shopkart.ui.activities.forgot_password.ForgotPasswordActivity
import com.example.shopkart.ui.activities.registration.RegistrationActivity
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private val mViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.viewModel = mViewModel

        mBinding.tvDontHaveAnAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        mBinding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        observeLiveEvents()
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.status.observe(this) { status ->
            when (status) {
                is Resource.Success -> {
                    showSnackBar(mBinding.root, status.data ?: "Success", false)
                    hideProgressbar()
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java).apply {
                        putExtra(
                            getString(R.string.prefIsProfileCompleted),
                            mViewModel.isProfileCompleted
                        )
                    })
                    finish()
                }
                is Resource.Error -> {
                    showSnackBar(
                        mBinding.root,
                        status.message ?: "An unknown error occurred.",
                        true
                    )
                    hideProgressbar()
                }
                is Resource.Loading -> {
                    showProgressbar()
                }
            }
        }
    }
}