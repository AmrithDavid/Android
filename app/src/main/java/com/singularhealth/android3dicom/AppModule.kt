package com.singularhealth.android3dicom.di

import android.content.Context
import com.singularhealth.android3dicom.data.CacheManager
import com.singularhealth.android3dicom.data.CacheManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCacheManager(
        @ApplicationContext context: Context,
    ): CacheManager = CacheManagerImpl(context)

    // can add more provider methods here for other dependencies
}
