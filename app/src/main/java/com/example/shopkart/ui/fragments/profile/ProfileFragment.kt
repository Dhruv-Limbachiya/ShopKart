package com.example.shopkart.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.databinding.FragmentProfileBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var mBinding: FragmentProfileBinding

    private val mViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.ivProfileImage.setOnClickListener {
            galleryLauncher.launch("image/*") // Launch the galleryLauncher with "image/*" type(only images).
        }

        observeLiveEvents()
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

    // Content Launcher.
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            mBinding.ivProfileImage.setImageURI(uri)
            mViewModel.observableProfileImageUri.set(uri.toString())
        }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }
}