package com.example.shopkart.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.databinding.FragmentSettingBinding
import com.example.shopkart.ui.activities.DashboardActivity
import com.example.shopkart.ui.activities.login.LoginActivity
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.ui.fragments.profile.ProfileViewModel
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Dhruv Limbachiya on 26-10-2021.
 */

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    lateinit var mBinding: FragmentSettingBinding
    private val mViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSettingBinding.inflate(inflater, container,false)

        mBinding.viewModel = mViewModel

        observeLiveEvents()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvSettingsAddresses.setOnClickListener {
            this.findNavController().navigate(
                SettingFragmentDirections.actionSettingFragmentToAddressFragment()
            )
        }
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.onLogoutClick.observe(viewLifecycleOwner) { isTrue ->
            if(isTrue) {
                // Navigates to the Login Activity.
                startActivity(
                    Intent(requireActivity(),LoginActivity::class.java)
                )
                requireActivity().finish() // Remove this activity from the back stack.
                mViewModel.resetClicks() // Resets click events
            }
        }

        mViewModel.onEditClick.observe(viewLifecycleOwner) { isTrue ->
            if(isTrue) {
                // Navigates to the profile screen.
                this@SettingFragment.findNavController().navigate(
                    R.id.action_global_profileFragment
                )
                mViewModel.resetClicks() // Resets click events
            }
        }
    }
}