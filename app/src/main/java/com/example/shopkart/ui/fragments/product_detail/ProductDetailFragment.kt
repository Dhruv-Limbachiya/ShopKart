package com.example.shopkart.ui.fragments.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.shopkart.R
import com.example.shopkart.data.model.CartItem
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

    private lateinit var product: Product

    private lateinit var productId: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        // Retrieve product id from the bundle.
        productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId

        userId = ProductDetailFragmentArgs.fromBundle(requireArguments()).userId

        // Get product details based on product id.
        mViewModel.getProductDetails(productId)

        observeLiveEvents()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.buttonAddToCart.setOnClickListener {
            val cartItem = CartItem(
                product.user_id,
                productId,
                product.title,
                product.price,
                product.image,
                DEFAULT_CART_QUANTITY,
                product.stock_quantity
            )
            mViewModel.onAddToCartButtonClick(cartItem)
        }
    }

    /**
     * Checks if product exist in cart.
     */
    private fun checkProductExistInCart() {
        mViewModel.checkProductAlreadyExistInCartItemInFireStore(product.id)
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.response.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    hideProgressbar()
                    product = status.data as Product
                    // Shows "Add To Cart" button only if product is not owned by current logged in user.
                    mBinding.buttonAddToCart.isVisible = product.user_id != mSharePreference.getString(R.string.prefUserId)

                    checkProductExistInCart()
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

        mViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    hideProgressbar()
                    showSnackBar(mBinding.root,status.data ?: "Success")
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

        mViewModel.statusBool.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    // TODO : Go to the cart screen.
                    mViewModel.observableGoToCartVisible.set(true)
                    mBinding.buttonAddToCart.isVisible = false // Hide the "Add To Cart" button if it is already added to cart.
                    showSnackBar(mBinding.root,status.data.toString() ?: "Status Bool Success")
                }
                is Resource.Error -> {
                    mViewModel.observableGoToCartVisible.set(false)
                    mBinding.buttonAddToCart.isVisible = product.user_id != mSharePreference.getString(R.string.prefUserId) // Hide the "Add To Cart" button.

                    showSnackBar(
                        mBinding.root,
                        status.message ?: "An unknown error occurred.",
                        true
                    )
                }
                else -> {/* No Operation */}
            }
        }
    }

    companion object {
        const val DEFAULT_CART_QUANTITY = "1"
    }
}