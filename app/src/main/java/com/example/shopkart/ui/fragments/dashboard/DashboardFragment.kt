package com.example.shopkart.ui.fragments.dashboard

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.FragmentDashboardBinding
import com.example.shopkart.ui.fragments.base.BaseFragment
import com.example.shopkart.util.Resource
import com.example.shopkart.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56.
 */

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    lateinit var mBinding: FragmentDashboardBinding

    private val mViewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var mAdapter: DashboardProductsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentDashboardBinding.inflate(inflater, container,false)
        observeLiveEvents()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllProducts()
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
    }

    /**
     * Helper method to show/hide recycler view and no record found textview.
     */
    private fun hideRecyclerViewShowNoRecordFound() {
        mBinding.rvDashboardItems.isVisible = false
        mBinding.tvNoItemsFound.isVisible = true
    }

    private fun showRecyclerViewHideNoRecordFound() {
        mBinding.rvDashboardItems.isVisible = true
        mBinding.tvNoItemsFound.isVisible = false
    }

    /**
     * Adds products in the recyclerview adapter.
     */
    private fun addDataToRecyclerView(products: List<Product>) {
        mBinding.rvDashboardItems.apply {
            adapter = mAdapter
            mAdapter.submitList(products)
            mAdapter.setOnProductItemClickListener {
                findNavController().navigate(
                    DashboardFragmentDirections.actionDashboardFragmentToProductDetailFragment(it.id,it.user_id)
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val addProductMenu = menu.findItem(R.id.menu_add_product)

        // If add product menu item is not null and visible then hide add product item menu.
        addProductMenu?.let {
            if(addProductMenu.isVisible) {
                addProductMenu.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // Navigates to the [SettingFragment] on setting menu item click
            R.id.menu_settings -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_settingFragment)
                return true
            }

            // Navigates to the [CartListFragment] on setting menu item click
            R.id.menu_cart -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_cartListFragment)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}