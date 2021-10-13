package com.example.shopkart.ui.activities.registration

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.shopkart.R
import com.example.shopkart.databinding.ActivityRegistrationBinding
import com.example.shopkart.ui.activities.BaseActivity
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar

class RegistrationActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding

    private val mViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_registration)
        mBinding.viewModel = mViewModel
        observeLiveEvents()
    }

    /**
     * Observe changes in the LiveData.
     */
    fun observeLiveEvents() {
        mViewModel.resource.observe(this) { res ->
            when(res) {
                is Resource.Success -> {
                    showSnackBar(mBinding.root,res.data ?: "Success",false)
                }
                is Resource.Error -> {
                    showSnackBar(mBinding.root,res.message ?: "An unknown error occurred.",true)
                }
                is Resource.Loading -> {
                    // Todo : show progress bar.
                }
            }
        }
    }
}