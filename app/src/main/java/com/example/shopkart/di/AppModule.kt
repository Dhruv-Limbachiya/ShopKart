package com.example.shopkart.di

import com.example.shopkart.data.firebase.FirebaseUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}