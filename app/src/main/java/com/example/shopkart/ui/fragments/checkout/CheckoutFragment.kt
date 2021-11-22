package com.example.shopkart.ui.fragments.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shopkart.databinding.FragmentCheckoutBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 22-11-2021 09:47 AM.
 */
class CheckoutFragment : BaseFragment() {

    private lateinit var mBinding: FragmentCheckoutBinding
    private val mViewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCheckoutBinding.inflate(inflater, container, false)
        mBinding.viewModel = mViewModel

        // Fetches address argument from the bundle.
        val address = CheckoutFragmentArgs.fromBundle(requireArguments()).address
        mViewModel.setCheckoutAddressDetails(address) // sets the address details into observables.

        return mBinding.root
    }
}