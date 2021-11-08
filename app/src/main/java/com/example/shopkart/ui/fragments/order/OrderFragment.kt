package com.example.shopkart.ui.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopkart.databinding.FragmentOrderBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56..
 */
@AndroidEntryPoint
class OrderFragment : BaseFragment() {

    lateinit var mBinding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentOrderBinding.inflate(inflater, container,false)

        return mBinding.root
    }
}