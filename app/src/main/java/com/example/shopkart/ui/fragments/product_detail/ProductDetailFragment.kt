package com.example.shopkart.ui.fragments.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    private lateinit var mProductOwnerId: String

    private lateinit var product: Product

    private lateinit var productId: String

    private var cartItemExist = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        // Retrieve product id from the bundle.
        productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId

        mProductOwnerId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productOwnerId

        // Get product details based on product id.
        mViewModel.getProductDetails(productId)

        checkProductExistInCart()

        observeLiveEvents()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.buttonAddToCart.setOnClickListener {

            val cartItem = CartItem(
                mSharePreference.getString(R.string.prefUserId),
                mProductOwnerId,
                productId,
                product.title,
                product.price,
                product.image,
                DEFAULT_CART_QUANTITY,
                product.stock_quantity
            )

            mViewModel.onAddToCartButtonClick(cartItem)
        }

        mBinding.buttonGoToCart.setOnClickListener {
            this.findNavController().navigate(
                ProductDetailFragmentDirections.actionProductDetailFragmentToCartListFragment()
            )
        }
    }

    /**
     * Checks if product exist in cart.
     */
    private fun checkProductExistInCart() {
         mViewModel.checkProductAlreadyExistInCartItemInFireStore(productId)
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

                    buttonVisibility()
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
                    cartItemExist = true
                    buttonVisibility()
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

        mViewModel.cartItemExist.observe(viewLifecycleOwner) {
            cartItemExist = it
            buttonVisibility()
        }
    }

    /**
     * Manages the visibility of "Add To Cart" and "Go To Cart" button.
     *  Show Add To Cart button on following cases
     *  Case 1 : If the product is not already added into the cart.
     *  Case 2 : If the product is not owned by the current user.
     */
    private fun buttonVisibility() {
        if(!cartItemExist && mProductOwnerId != mSharePreference.getString(R.string.prefUserId) && !mViewModel.observableOutOfStock.get()) {
            mViewModel.observableAddToCartButtonVisible.set(true)
            mViewModel.observableGoToCartVisible.set(false)
        } else if(cartItemExist) {
            mViewModel.observableGoToCartVisible.set(true)
            mViewModel.observableAddToCartButtonVisible.set(false)
        } else {
            mViewModel.observableGoToCartVisible.set(false)
            mViewModel.observableAddToCartButtonVisible.set(false)
        }
    }


    companion object {
        const val DEFAULT_CART_QUANTITY = "1"
    }
}