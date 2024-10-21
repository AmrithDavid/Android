package com.singularhealth.android3dicom.di

import android.content.Context
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.DataStoreRepository
import com.singularhealth.android3dicom.network.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppStateModule {
    @Singleton
    @Provides
    fun provideAppState(
        @ApplicationContext context: Context,
        dataStoreRepository: DataStoreRepository,
        networkClient: NetworkClient,
    ): AppState = AppState(context, dataStoreRepository, networkClient)
}
