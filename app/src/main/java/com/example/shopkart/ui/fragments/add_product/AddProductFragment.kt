package com.example.shopkart.ui.fragments.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopkart.databinding.FragmentAddProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created by Dhruv Limbachiya on 28-10-2021.
 */
class AddProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentAddProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentAddProductBinding.inflate(inflater, container, false)

        return mBinding.root
    }
}