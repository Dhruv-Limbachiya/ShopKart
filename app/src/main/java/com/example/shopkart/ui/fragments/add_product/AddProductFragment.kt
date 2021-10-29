package com.example.shopkart.ui.fragments.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.databinding.FragmentAddProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import com.example.shopkart.util.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Dhruv Limbachiya on 28-10-2021.
 */

@AndroidEntryPoint
class AddProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentAddProductBinding
    private val mViewModel: AddProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentAddProductBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        mBinding.ivProductImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        observeLiveEvents()

        return mBinding.root
    }


    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Success -> {
                    hideProgressbar()
                    showToast(requireContext(),status.data ?: "Success")
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
                is Resource.Loading -> showProgressbar()
            }
        }
    }

    // Content Launcher.
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                mBinding.ivProductImage.setImageURI(it)
                mViewModel.observableProductImageUri.set(it.toString())
                mViewModel.observableImageAttached.set(true)
            }
        }
}