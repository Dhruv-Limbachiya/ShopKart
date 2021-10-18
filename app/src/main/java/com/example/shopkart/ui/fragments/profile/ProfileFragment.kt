package com.example.shopkart.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopkart.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding
    private val mViewModel: ProfileViewModel by viewModels()

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
    }

    // Content Launcher.
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            mBinding.ivProfileImage.setImageURI(uri)
        }
}