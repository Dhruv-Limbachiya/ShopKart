package com.example.shopkart.ui.fragments.my_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.R
import com.example.shopkart.data.model.Address
import com.example.shopkart.databinding.FragmentMyAddressBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.SwipeGestureCallback
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

    private lateinit var mEditTouchHelper: ItemTouchHelper

    private lateinit var mDeleteTouchHelper: ItemTouchHelper

    private lateinit var swipeRightToDeleteHandler: SwipeGestureCallback

    private lateinit var swipeLeftToEditHandler: SwipeGestureCallback

    private var isFromCartToAddressFlow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyAddressBinding.inflate(inflater, container, false)

        isFromCartToAddressFlow = MyAddressFragmentArgs.fromBundle(requireArguments()).selectAddress

        observeLiveEvents()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnAddAddress.setOnClickListener {
            this.findNavController().navigate(
                MyAddressFragmentDirections.actionAddressFragmentToAddressDetailFragment(
                    title = getString(
                        R.string.title_add_address
                    )
                )
            )
        }

        enableSwipeGestures()

        // Edit Item Touch Helper instance
        mEditTouchHelper = ItemTouchHelper(swipeLeftToEditHandler)

        // Delete Item Touch Helper instance
        mDeleteTouchHelper = ItemTouchHelper(swipeRightToDeleteHandler)
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
                }
                is Resource.Loading -> showProgressbar()
            }
        }

        mViewModel.status.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    mViewModel.getMyAddresses()
                    showSnackBar(mBinding.root, response.data ?: "Success")
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
     * Adds addresses in the recyclerview.
     */
    private fun addDataToRecyclerView(address: List<Address>) {
        mBinding.rvAddress.apply {
            adapter = mAdapter
            mAdapter.submitList(address)
        }

        /**
         * isFromCartToAddressFlow : Checks if user navigated here to select address for checkout purpose ,
         * if true then navigates to [CheckoutFragment] with address as a parameter.
         */
        if(isFromCartToAddressFlow) {
            mAdapter.setOnAddressSelectedListener {
                // Navigates to [CheckoutFragment]
                this.findNavController()
                    .navigate(
                        MyAddressFragmentDirections.actionAddressFragmentToCheckoutFragment(it)
                    )
            }
        }

        // Attaches ItemTouchHelper to Recyclerview.
        mEditTouchHelper.attachToRecyclerView(mBinding.rvAddress)

        mDeleteTouchHelper.attachToRecyclerView(mBinding.rvAddress)
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

    /**
     * Enables and configures swipe gestures for the Recyclerview item.
     */
    private fun enableSwipeGestures() {
        //Handles Left swipe gesture. Navigates to address detail screen.
        swipeLeftToEditHandler = object : SwipeGestureCallback(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)!!,
            ItemTouchHelper.RIGHT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val addressToEdit = mAdapter.currentList[viewHolder.adapterPosition]
                findNavController().navigate(
                    MyAddressFragmentDirections.actionAddressFragmentToAddressDetailFragment(
                        title = getString(R.string.title_edit_address),
                        address = addressToEdit
                    )
                )
            }
        }

        // Handles Right swipe gesture. Delete address from address collection on Firestore db.
        swipeRightToDeleteHandler = object : SwipeGestureCallback(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!,
            ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val addressToDelete = mAdapter.currentList[viewHolder.adapterPosition]
                mViewModel.deleteAddress(addressToDelete.id)
            }
        }
    }

}