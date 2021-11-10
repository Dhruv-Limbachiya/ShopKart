package com.example.shopkart.ui.fragments.product

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.FragmentProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56..
 */

@AndroidEntryPoint
class ProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentProductBinding

    private val mViewModel: ProductViewModel by viewModels()

    @Inject
    lateinit var mAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentProductBinding.inflate(inflater, container,false)
        observeLiveEvents()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getProducts()
    }

    /**
     * Observe changes in the LiveData.
     */
    private fun observeLiveEvents() {
        mViewModel.response.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    val products = response.data as List<Product>
                    if(products.isNotEmpty()) {
                        addDataToRecyclerView(products)
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
                is Resource.Loading ->  showProgressbar()
            }
        }

        mViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading ->  showProgressbar()

                is Resource.Success -> {
                    hideProgressbar()
                    showSnackBar(mBinding.root, status.data ?: "Success", false)
                }
                is Resource.Error -> {
                    hideProgressbar()
                    showSnackBar(
                        mBinding.root,
                        status.message ?: "An unknown error occurred.",
                        true
                    )
                }

            }
        }
    }

    /**
     * Helper method to show/hide recycler view and no record found textview.
     */
    private fun hideRecyclerViewShowNoRecordFound() {
        mBinding.rvProducts.isVisible = false
        mBinding.tvNoProductsFound.isVisible = true
    }

    private fun showRecyclerViewHideNoRecordFound() {
        mBinding.rvProducts.isVisible = true
        mBinding.tvNoProductsFound.isVisible = false
    }

    /**
     * Adds products in the recyclerview.
     */
    private fun addDataToRecyclerView(products: List<Product>) {
        mBinding.rvProducts.apply {
            adapter = mAdapter
            mAdapter.submitList(products)
            mAdapter.setDeleteProductListener { product ->
                showDeleteAlertDialogBox(product)
            }
            mAdapter.setOnProductItemClickListener {
                findNavController().navigate(
                    ProductFragmentDirections.actionProductFragmentToProductDetailFragment(it.id,it.user_id)
                )
            }
        }
    }

    /**
     * Shows an alert dialog box on clicking delete icon.
     */
    private fun showDeleteAlertDialogBox(product: Product) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage("Are you sure you want to delete \"${product.title}\" product?")
        builder.setPositiveButton("Yes") { dialog, p1 ->
            mViewModel.deleteProduct(product.id)
            mViewModel.getProducts()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, p1 ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val settingMenu = menu.findItem(R.id.menu_settings)
        val cartMenu = menu.findItem(R.id.menu_cart)

        // If add product menu item is not null and visible then hide add product menu item.
        settingMenu?.let {
            if(settingMenu.isVisible) {
                settingMenu.isVisible = false
            }
        }

        // If cart menu item is not null and visible then hide cart menu item.
        cartMenu?.let {
            if(cartMenu.isVisible) {
                cartMenu.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // Navigates to the [SettingFragment] on setting menu item click
            R.id.menu_add_product -> {
                findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}