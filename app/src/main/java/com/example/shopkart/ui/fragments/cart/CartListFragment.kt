package com.example.shopkart.ui.fragments.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.FragmentCartListBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.product.ProductFragmentDirections
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 03-11-2021 18:38.
 */

@AndroidEntryPoint
class CartListFragment : BaseFragment() {

    private lateinit var mBinding: FragmentCartListBinding

    private val mViewModel: CartViewModel by viewModels()

    @Inject
    lateinit var mAdapter: CartListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentCartListBinding.inflate(inflater, container, false)
        mBinding.viewModel = mViewModel

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
                    showRecyclerViewHideNoRecordFound()
                }
                is Resource.Error -> {
                    hideProgressbar()
                    hideRecyclerViewShowNoRecordFound()
                    showSnackBar(
                        mBinding.root,
                        response.message ?: "An unknown error occurred.",
                        true
                    )
                }
                is Resource.Loading -> showProgressbar()
            }
        }
    }

    /**
     * Adds products in the recyclerview adapter.
     */
    private fun addDataToRecyclerView(cartItems: List<CartItem>) {
        mBinding.rvCartItems.apply {
            adapter = mAdapter
            mAdapter.submitList(cartItems)
            mAdapter.setDeleteProductListener { cartItem ->
                showDeleteAlertDialogBox(cartItem)
            }
        }
    }

    /**
     * Shows an alert dialog box on clicking delete icon.
     */
    private fun showDeleteAlertDialogBox(cartItem: CartItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage("Are you sure you want to remove \"${cartItem.title}\" from the cart ?")
        builder.setPositiveButton("Yes") { dialog, p1 ->
            cartItem.id?.let {
                mViewModel.removeCartItem(it) // Remove cart item.
                mViewModel.refreshCartItemList() // Refresh the entire list.
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, p1 ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    /**
     * Helper method to show/hide recycler view and no record found textview.
     */
    private fun hideRecyclerViewShowNoRecordFound() {
        mBinding.rvCartItems.isVisible = false
        mBinding.layoutPricingDetails.isVisible = false
        mBinding.tvNoCartItemFound.isVisible = true
    }

    private fun showRecyclerViewHideNoRecordFound() {
        mBinding.rvCartItems.isVisible = true
        mBinding.layoutPricingDetails.isVisible = true
        mBinding.tvNoCartItemFound.isVisible = false
    }

}