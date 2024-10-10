package com.singularhealth.android3dicom.di

import android.content.Context
import com.singularhealth.android3dicom.utilities.CacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheManagerModule {
    @Singleton
    @Provides
    fun provideCacheManager(
        @ApplicationContext context: Context,
    ): CacheManager = CacheManager(context)
}
