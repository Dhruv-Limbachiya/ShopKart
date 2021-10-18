package com.example.shopkart.di

import android.content.Context
import com.example.shopkart.ShopKartApplication
import com.example.shopkart.data.firebase.FirebaseUtil
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
}