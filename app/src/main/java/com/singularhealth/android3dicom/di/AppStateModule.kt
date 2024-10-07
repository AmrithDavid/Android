package com.singularhealth.android3dicom.di

import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppStateModule {
    @Singleton
    @Provides
    fun provideAppState(dataStoreRepository: DataStoreRepository): AppState = AppState(dataStoreRepository)
}
