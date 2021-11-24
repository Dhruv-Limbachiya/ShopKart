package com.example.shopkart.ui.fragments.sold_prouct_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shopkart.databinding.FragmentSoldProductDetailBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 24-11-2021 12:36 PM.
 */
class SoldProductDetailFragment : BaseFragment() {

    lateinit var mBinding: FragmentSoldProductDetailBinding

    private val mViewModel: SoldProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSoldProductDetailBinding.inflate(inflater, container, false)
        mBinding.viewModel = mViewModel

        val soldProduct = SoldProductDetailFragmentArgs.fromBundle(requireArguments()).product
        mViewModel.setSoldProductDetails(soldProduct)

        return mBinding.root
    }

}