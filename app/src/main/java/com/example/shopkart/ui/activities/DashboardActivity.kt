package com.example.shopkart.ui.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.shopkart.R
import com.example.shopkart.ui.activities.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        intent?.let {
            val isProfileCompleted = it.getIntExtra(getString(R.string.prefIsProfileCompleted),-1)
            if(isProfileCompleted == 0) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

                navController = navHostFragment.navController
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