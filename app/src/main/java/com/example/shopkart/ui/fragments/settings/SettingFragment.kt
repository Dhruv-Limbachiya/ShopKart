package com.example.shopkart.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.shopkart.databinding.FragmentSettingBinding
import com.example.shopkart.ui.activities.DashboardActivity
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created by Dhruv Limbachiya on 26-10-2021.
 */
class SettingFragment : BaseFragment() {

    lateinit var mBinding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSettingBinding.inflate(inflater, container,false)

        return mBinding.root
    }
}