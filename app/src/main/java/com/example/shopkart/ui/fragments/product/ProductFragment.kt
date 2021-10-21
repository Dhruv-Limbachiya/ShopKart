package com.example.shopkart.ui.fragments.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopkart.databinding.FragmentProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56..
 */
class ProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductBinding.inflate(inflater, container,false)

        return mBinding.root
    }
}