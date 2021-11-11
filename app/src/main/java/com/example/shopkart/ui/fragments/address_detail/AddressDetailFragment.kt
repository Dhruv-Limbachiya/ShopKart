package com.example.shopkart.ui.fragments.address_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.data.model.Address
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.databinding.FragmentAddressDetailBinding
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Dhruv Limbachiya on 09-11-2021 14:22.
 */

@AndroidEntryPoint
class AddressDetailFragment : BaseFragment() {

    private lateinit var mBinding: FragmentAddressDetailBinding
    private val mViewModel: AddressDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentAddressDetailBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        observeLiveEvents()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = AddressDetailFragmentArgs.fromBundle(requireArguments()).address

        setButtonText(address)

        address?.let {
            mViewModel.setAddressInfo(it)
        }

    }

    /**
     * Sets button text according to user action (Edit or Add).
     */
    private fun setButtonText(address: Address?) {
        if(address != null) {
            mViewModel.observableButtonName.set(getString(R.string.text_update))
        } else {
            mViewModel.observableButtonName.set(getString(R.string.text_submit))
        }
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    hideProgressbar()
                    showSnackBar(mBinding.root, status.data ?: "Success", false)
                    this.findNavController().popBackStack() // Back to the previous fragment.
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