package com.example.shopkart.ui.fragments.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.databinding.FragmentCheckoutBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.cart.CartListAdapter
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 22-11-2021 09:47 AM.
 */
@AndroidEntryPoint
class CheckoutFragment : BaseFragment() {

    private lateinit var mBinding: FragmentCheckoutBinding

    private val mViewModel: CheckoutViewModel by viewModels()

    @Inject
    lateinit var mAdapter: CartListAdapter

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

        observeLiveEvents()

        return mBinding.root
    }

    /**
     * Observe the Live events
     */
    private fun observeLiveEvents() {
        mViewModel.cartItemStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    addDataToRecyclerView(response.data!!)
                }
                is Resource.Error -> {
                    hideProgressbar()
                    showSnackBar(
                        mBinding.root,
                        response.message ?: "An unknown error occurred.",
                        true
                    )
                }
                is Resource.Loading -> showProgressbar()
            }
        }

        mViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> showProgressbar()

                is Resource.Success -> {
                    hideProgressbar()
                    showSnackBar(mBinding.root, status.data ?: "Success", false)
                    findNavController().popBackStack(R.id.dashboardFragment,false)
                }
                is Resource.Error -> {
                    hideProgressbar()
                    showSnackBar(
                        mBinding.root,
                        status.message ?: "An unknown error occurred.",
                        true
                    )
                }

            }
        }
    }

    /**
     * Adds cart items in the recyclerview adapter.
     */
    private fun addDataToRecyclerView(cartItems: List<CartItem>) {
        hideProgressbar()
        mBinding.rvCartListItems.apply {
            adapter = mAdapter
            mAdapter.setIsEditable(false)
            mAdapter.submitList(cartItems)
        }
    }

}