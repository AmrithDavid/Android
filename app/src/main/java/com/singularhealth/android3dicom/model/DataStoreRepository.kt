package com.singularhealth.android3dicom.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

interface IDataStoreRepository {
    suspend fun setString(
        key: String,
        value: String,
    )

    suspend fun getString(key: String): String?

    suspend fun setInt(
        key: String,
        value: Int,
    )

    suspend fun getInt(key: String): Int?

    fun getInstance(): DataStore<Preferences>
}

class DataStoreRepository
    @Inject
    constructor(
        private val context: Context,
    ) : IDataStoreRepository {
        override fun getInstance(): DataStore<Preferences> = context.dataStore

        override suspend fun setString(
            key: String,
            value: String,
        ) {
            val preferencesKey = stringPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[preferencesKey] = value
            }
        }

        override suspend fun getString(key: String): String? {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            return preferences[preferencesKey]
        }

        override suspend fun setInt(
            key: String,
            value: Int,
        ) {
            val preferencesKey = intPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[preferencesKey] = value
            }
        }

        override suspend fun getInt(key: String): Int? {
            val preferencesKey = intPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            return preferences[preferencesKey]
        }
    }
