package com.example.shopkart.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.databinding.FragmentSettingBinding
import com.example.shopkart.ui.activities.DashboardActivity
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Dhruv Limbachiya on 26-10-2021.
 */

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    lateinit var mBinding: FragmentSettingBinding
    private val mViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSettingBinding.inflate(inflater, container,false)
        mBinding.viewModel = mViewModel

        return mBinding.root
    }
}