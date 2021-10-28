package com.example.shopkart.ui.fragments.dashboard

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.databinding.FragmentDashboardBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56.
 */
class DashboardFragment : BaseFragment() {

    lateinit var mBinding: FragmentDashboardBinding

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

        return mBinding.root
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
        }
        return super.onOptionsItemSelected(item)
    }

}