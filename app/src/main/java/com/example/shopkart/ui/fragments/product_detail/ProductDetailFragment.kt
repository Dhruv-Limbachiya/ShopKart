package com.example.shopkart.ui.fragments.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shopkart.databinding.FragmentProductDetailBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Dhruv Limbachiya on 01-11-2021.
 */

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment() {

    lateinit var mBinding: FragmentProductDetailBinding

    private val mViewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        // Retrieve product id from the bundle.
        val productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId

        // Get product details based on product id.
        mViewModel.getProductDetails(productId)

        observeLiveEvents()

        return mBinding.root
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.response.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    hideProgressbar()
                }
                is Resource.Error -> {
                    hideProgressbar()
                    showSnackBar(
                        mBinding.root,
                        status.message ?: "An unknown error occurred.",
                        true
                    )
                }
                is Resource.Loading -> {
                    showProgressbar()
                }
            }
        }
    }
}