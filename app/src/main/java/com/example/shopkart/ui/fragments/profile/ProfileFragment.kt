package com.example.shopkart.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopkart.R
import com.example.shopkart.databinding.ActivityLoginBinding
import com.example.shopkart.databinding.FragmentProfileBinding
import com.example.shopkart.ui.activities.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding
    private val mViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater,container, false)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }
}