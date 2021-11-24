package com.example.shopkart.di

import android.content.Context
import com.example.shopkart.ShopKartApplication
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.fragments.cart.CartListAdapter
import com.example.shopkart.ui.fragments.dashboard.DashboardProductsAdapter
import com.example.shopkart.ui.fragments.my_address.AddressListAdapter
import com.example.shopkart.ui.fragments.order.OrderListAdapter
import com.example.shopkart.ui.fragments.product.ProductsAdapter
import com.example.shopkart.ui.fragments.sold_product.SoldProductListAdapter
import com.example.shopkart.util.SharePreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Dhruv Limbachiya on 14-10-2021 12:30.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseUtil() = FirebaseUtil()

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context) = SharePreferenceUtil(context)

    @Singleton
    @Provides
    fun provideApplication() = ShopKartApplication()

    @Singleton
    @Provides
    fun provideProductsAdapter() = ProductsAdapter()

    @Singleton
    @Provides
    fun provideDashboardProductsAdapter() = DashboardProductsAdapter()

    @Singleton
    @Provides
    fun provideCartListAdapter() = CartListAdapter()

    @Singleton
    @Provides
    fun provideAddressListAdapter() = AddressListAdapter()

    @Singleton
    @Provides
    fun provideOrderListAdapter() = OrderListAdapter()

    @Singleton
    @Provides
    fun provideSoldProductListAdapter() = SoldProductListAdapter()
}