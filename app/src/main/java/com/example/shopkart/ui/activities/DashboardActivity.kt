package com.example.shopkart.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard)

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

        supportActionBar?.setBackgroundDrawable(
            getDrawable(R.drawable.drawable_gradient_splash_background)
        )

        // set nav controller and app bar configuration instance to the default action bar.
        setupActionBarWithNavController(navController, appBarConfiguration)

        // set nav controller to bottom navigation view for automatically navigating between fragments.
        mBinding.bottomNavView.setupWithNavController(navController)
    }

    /**
     * Navigates to [ProfileFragment] if the profile is incomplete.
     */
    private fun navigateToProfileOnInCompleteProfile() {
        intent?.let {
            val isProfileCompleted = it.getIntExtra(getString(R.string.prefIsProfileCompleted), -1)
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
}