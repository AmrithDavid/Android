package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.LoginRequest
import com.singularhealth.android3dicom.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "settings")

class LoginViewModel(
    private val context: Context,
) : ViewModel() {
    private val apiService: ApiService

    companion object {
        // Set this to true to work on the login UI
        private const val FORCE_LOGIN_SCREEN = true
    }

    private val _isDebugMode = MutableStateFlow(FORCE_LOGIN_SCREEN)
    val isDebugMode: StateFlow<Boolean> = _isDebugMode.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                // Only check logged in state if not in debug mode
                if (_isDebugMode.value) {
                    false
                } else {
                    preferences[booleanPreferencesKey("is_logged_in")] ?: false
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://api.singular.health/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun loginUser(
        email: String,
        password: String,
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val loginRequest = LoginRequest(email, password)
                Log.d("LoginViewModel", "Sending login request: $loginRequest")
                val response = apiService.login(loginRequest)
                Log.d("LoginViewModel", "Received response: $response")

                if (response.access_token != null) {
                    // Save the access token
                    context.dataStore.edit { preferences ->
                        preferences[stringPreferencesKey("access_token")] = response.access_token
                        preferences[booleanPreferencesKey("is_logged_in")] = true
                    }
                    Log.d("LoginViewModel", "Login successful, token saved")
                    true
                } else {
                    Log.e("LoginViewModel", "Login failed: No access token in response")
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error during login", e)
                when (e) {
                    is IOException -> Log.e("LoginViewModel", "Network error: ${e.message}")
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("LoginViewModel", "HTTP error ${e.code()}: $errorBody")
                    }
                    else -> Log.e("LoginViewModel", "Unexpected error: ${e.message}")
                }
                false
            }
        }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("access_token"))
            preferences[booleanPreferencesKey("is_logged_in")] = false
        }
        Log.d("LoginViewModel", "User logged out")
    }

    // This function is kept for potential future use
    fun toggleDebugMode() {
        _isDebugMode.value = !_isDebugMode.value
        Log.d("LoginViewModel", "Debug mode toggled. New value: ${_isDebugMode.value}")
    }
}

class LoginViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
