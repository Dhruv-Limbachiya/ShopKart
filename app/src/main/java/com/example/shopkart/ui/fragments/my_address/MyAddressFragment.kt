package com.example.shopkart.ui.fragments.my_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.data.model.Address
import com.example.shopkart.databinding.FragmentMyAddressBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 09-11-2021 11:51.
 */

@AndroidEntryPoint
class MyAddressFragment : BaseFragment() {

    private lateinit var mBinding: FragmentMyAddressBinding

    private val mViewModel: MyAddressViewModel by viewModels()

    @Inject
    lateinit var mAdapter: AddressListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyAddressBinding.inflate(inflater, container, false)
        observeLiveEvents()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnAddAddress.setOnClickListener {
            this.findNavController().navigate(
                MyAddressFragmentDirections.actionAddressFragmentToAddressDetailFragment()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getMyAddresses()
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.addressList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    val address = response.data as List<Address>
                    if (address.isNotEmpty()) {
                        addDataToRecyclerView(address)
                        showRecyclerViewHideNoRecordFound()
                    } else {
                        hideRecyclerViewShowNoRecordFound()
                    }
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
     * Adds addresses in the recyclerview.
     */
    private fun addDataToRecyclerView(address: List<Address>) {
        mBinding.rvAddress.apply {
            adapter = mAdapter
            mAdapter.submitList(address)
        }
    }

    /**
     * Helper method to show/hide recycler view and no record found textview.
     */
    private fun hideRecyclerViewShowNoRecordFound() {
        mBinding.rvAddress.isVisible = false
        mBinding.tvNoAddressesFound.isVisible = true
    }

    private fun showRecyclerViewHideNoRecordFound() {
        mBinding.rvAddress.isVisible = true
        mBinding.tvNoAddressesFound.isVisible = false
    }
}