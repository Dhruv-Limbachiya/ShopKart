package com.example.shopkart.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopkart.R
import com.example.shopkart.databinding.ActivityDashboardBinding
import com.example.shopkart.ui.activities.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var mBinding: ActivityDashboardBinding
    private var isProfileCompleted = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard)

        setSupportActionBar(mBinding.appBar.toolbar)

        setUpNavComp()

        navigateToProfileOnInCompleteProfile()
    }

    /**
     * Configures the require navigation stuff.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpNavComp() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment // Nav Host

        navController = navHostFragment.navController // find NavController using NavHostFragment/

        // Configure App bar by specifying all top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment, R.id.productFragment, R.id.orderFragment
            )
        )

        mBinding.appBar.toolbar.setupWithNavController(navController,appBarConfiguration)

        // set nav controller and app bar configuration instance to the default action bar.
        setupActionBarWithNavController(navController, appBarConfiguration)

        // set nav controller to bottom navigation view for automatically navigating between fragments.
        mBinding.bottomNavView.setupWithNavController(navController)

        handleDestinationChangeEvents()
    }

    private fun handleDestinationChangeEvents() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingFragment -> {
                    changeTitle(getString(R.string.text_settings))
                    hideBottomBar()
                }

                R.id.dashboardFragment -> {
                    changeTitle(getString(R.string.text_dashboard))
                    showBottomBar()
                }

                R.id.productFragment -> {
                    changeTitle(getString(R.string.text_product))
                    showBottomBar()
                }

                R.id.orderFragment -> {
                    changeTitle(getString(R.string.text_order))
                    showBottomBar()
                }

                R.id.profileFragment -> {
                    // Displays Complete Profile if user haven't completed all the profile details.
                    if (isProfileCompleted == 0) {
                        changeTitle(getString(R.string.text_complete_profile))
                    } else {
                        changeTitle(getString(R.string.text_profile))
                    }

                    hideBottomBar()
                }
            }
        }
    }

    private fun changeTitle(title: String) {
        mBinding.appBar.tvTitle.text = title
    }

    /**
     * Navigates to [ProfileFragment] if the profile is incomplete.
     */
    private fun navigateToProfileOnInCompleteProfile() {
        intent?.let {
            isProfileCompleted = it.getIntExtra(getString(R.string.prefIsProfileCompleted), -1)
            if (isProfileCompleted == 0) {
                navController
                    .navigate(R.id.action_global_profileFragment)
            }
        }
    }

    override fun onBackPressed() {
        if(!navController.popBackStack())
        exitOnDoubleBackPress()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // Navigates to the [SettingFragment] on setting menu item click
            R.id.menu_settings -> {
                navController.navigate(R.id.action_global_settingFragment)
                changeTitle(getString(R.string.text_settings))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // Show the bottom bar.
    private fun showBottomBar(){
        mBinding.bottomNavView.isVisible = true
    }

    // Hide the bottom bar.
    private fun hideBottomBar(){
        mBinding.bottomNavView.isVisible = false
    }
}