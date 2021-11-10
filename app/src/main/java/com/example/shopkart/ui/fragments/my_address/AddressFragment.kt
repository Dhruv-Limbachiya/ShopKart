package com.example.shopkart.ui.fragments.my_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.shopkart.databinding.FragmentAddressBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 09-11-2021 11:51.
 */
class AddressFragment : BaseFragment() {

    private lateinit var mBinding: FragmentAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddressBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnAddAddress.setOnClickListener {
            this.findNavController().navigate(
                    AddressFragmentDirections.actionAddressFragmentToAddressDetailFragment()
                )
        }
    }
}