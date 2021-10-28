package com.example.shopkart.ui.fragments.product

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.shopkart.R
import com.example.shopkart.databinding.FragmentProductBinding
import com.example.shopkart.ui.fragments.base.BaseFragment

/**
 * Created By Dhruv Limbachiya on 21-10-2021 10:56..
 */
class ProductFragment : BaseFragment() {

    lateinit var mBinding: FragmentProductBinding

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

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val settingMenu = menu.findItem(R.id.menu_settings)
        // If add product menu item is not null and visible then hide add product item menu.
        settingMenu?.let {
            if(settingMenu.isVisible) {
                settingMenu.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // Navigates to the [SettingFragment] on setting menu item click
            R.id.menu_add_product -> {
                findNavController().navigate(R.id.action_global_settingFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}