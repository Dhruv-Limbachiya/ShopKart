package com.example.shopkart.ui.fragments.sold_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.shopkart.data.model.Order
import com.example.shopkart.data.model.SoldProduct
import com.example.shopkart.databinding.FragmentOrderBinding
import com.example.shopkart.databinding.FragmentSoldProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.order.OrderFragmentDirections
import com.example.shopkart.ui.fragments.order.OrderListAdapter
import com.example.shopkart.ui.fragments.order.OrderViewModel
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 24-11-2021 11:14 AM.
 */
@AndroidEntryPoint
class SoldProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentSoldProductBinding

    private val mViewModel: SoldProductViewModel by viewModels()

    @Inject
    lateinit var mAdapter: SoldProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSoldProductBinding.inflate(inflater, container, false)

        observeLiveEvents()

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getMySoldProducts()
    }

    /**
     * Observe the Live events
     */
    private fun observeLiveEvents() {
        mViewModel.soldProductStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    showRecyclerViewHideNoRecordFound()
                    addDataToRecyclerView(response.data!!)
                }
                is Resource.Error -> {
                    hideRecyclerViewShowNoRecordFound()
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
    }

    /**
     * Adds products in the recyclerview adapter.
     */
    private fun addDataToRecyclerView(products: List<SoldProduct>) {
        hideProgressbar()
        mBinding.rvSoldProducts.apply {
            adapter = mAdapter
            if (products.isNotEmpty()) {
                mAdapter.submitList(products)
            }

            mAdapter.setOnSoldProductItemClickListener {
                this.findNavController()
                    .navigate(SoldProductFragmentDirections.actionSoldProductFragmentToSoldProductDetailFragment(it))
            }
        }
    }

    /**
     * Helper method to show/hide recycler view and no record found textview.
     */
    private fun hideRecyclerViewShowNoRecordFound() {
        mBinding.rvSoldProducts.isVisible = false
        mBinding.tvNoProductSoldYet.isVisible = true
        hideProgressbar()
    }

    private fun showRecyclerViewHideNoRecordFound() {
        mBinding.rvSoldProducts.isVisible = true
        mBinding.tvNoProductSoldYet.isVisible = false
        hideProgressbar()
    }
}