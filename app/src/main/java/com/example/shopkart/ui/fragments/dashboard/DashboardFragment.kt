package com.example.shopkart.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopkart.databinding.FragmentDashboardBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56.
 */
class DashboardFragment : BaseFragment() {

    lateinit var mBinding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentDashboardBinding.inflate(inflater, container,false)

        return mBinding.root
    }
}