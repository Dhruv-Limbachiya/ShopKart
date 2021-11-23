package com.example.shopkart.ui.fragments.order_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shopkart.data.model.Order
import com.example.shopkart.databinding.FragmentOrderDetailBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.cart.CartListAdapter
import com.example.shopkart.ui.fragments.order.OrderListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 23-11-2021 12:42 PM.
 */

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment() {

    lateinit var mBinding: FragmentOrderDetailBinding

    private val mViewModel: OrderDetailViewModel by viewModels()

    private lateinit var order: Order

    @Inject
    lateinit var mAdapter: CartListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        order = OrderDetailFragmentArgs.fromBundle(requireArguments()).order

        setOrderDetails(order)

        return mBinding.root
    }

    /**
     * Set the order details in all the views.
     */
    private fun setOrderDetails(order: Order) {
        mBinding.rvCartListItems.apply {
            adapter = mAdapter
            mAdapter.setIsEditable(false)
            mAdapter.submitList(order.items)
        }

        mViewModel.setOtherDetailsIntoObservables(order)
    }
}