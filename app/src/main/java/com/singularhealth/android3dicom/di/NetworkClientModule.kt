package com.singularhealth.android3dicom.di

import com.singularhealth.android3dicom.model.DataStoreRepository
import com.singularhealth.android3dicom.network.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkClientModule {
    @Singleton
    @Provides
    fun provideNetworkClient(dataStoreRepository: DataStoreRepository): NetworkClient = NetworkClient(dataStoreRepository)
}
