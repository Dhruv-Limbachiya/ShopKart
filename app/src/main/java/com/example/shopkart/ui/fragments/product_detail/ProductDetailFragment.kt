package com.example.shopkart.ui.fragments.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.shopkart.R
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.FragmentProductDetailBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.SharePreferenceUtil
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 01-11-2021.
 */

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment() {

    private lateinit var mBinding: FragmentProductDetailBinding

    private val mViewModel: ProductDetailViewModel by viewModels()

    @Inject
    lateinit var mSharePreference: SharePreferenceUtil

    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        // Retrieve product id from the bundle.
        val productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId

        userId = ProductDetailFragmentArgs.fromBundle(requireArguments()).userId

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
                    val product = status.data as Product
                    // Shows "Add To Cart" button only if product is not owned by current logged in user.
                    mBinding.buttonAddToCart.isVisible = product.user_name != mSharePreference.getString(R.string.prefUserId)
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