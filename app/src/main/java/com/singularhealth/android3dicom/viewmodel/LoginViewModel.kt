package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.network.ISingularHealthRestService
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        private val singularHealthRestService: ISingularHealthRestService
        private val dataStore = appState.dataStore

        private val _isLoading = MutableStateFlow<Boolean>(false)
        val isLoading: StateFlow<Boolean> get() = _isLoading

        private val _errorMessage = MutableStateFlow<String>("")
        val errorMessage: StateFlow<String> get() = _errorMessage

        private val _email = MutableStateFlow<String>("")
        val email: StateFlow<String> get() = _email

        private val _password = MutableStateFlow<String>("")
        val password: StateFlow<String> get() = _password

        companion object {
            // Set this to true to work on the login UI
            private const val FORCE_LOGIN_SCREEN = false
        }

        private val _isDebugMode = MutableStateFlow(FORCE_LOGIN_SCREEN)
        val isDebugMode: StateFlow<Boolean> = _isDebugMode.asStateFlow()

        val isLoggedIn: StateFlow<Boolean> =
            dataStore
                .getInstance()
                .data
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

            singularHealthRestService = retrofit.create(ISingularHealthRestService::class.java)
        }

        fun logoutUser() {
            appState.logout()
        }

        private fun onLoginComplete(isSuccess: Boolean) {
            _isLoading.value = false
            if (isSuccess) {
                if (appState.loginPreference == LoginPreferenceOption.NONE) {
                    appState.navigateTo(ViewRoute.LOGIN_SETUP)
                } else {
                    appState.navigateTo(ViewRoute.SCAN_LIBRARY)
                }
            } else {
                _errorMessage.value = "Login failed"
            }
        }

        fun onLoginPressed() {
            _errorMessage.value = ""
            _isLoading.value = true
            viewModelScope.launch {
                appState.login(email.value, password.value) { onLoginComplete(it) }
            }
        }

        fun onEmailChanged(value: String) {
            _email.value = value
        }

        fun onPasswordChanged(value: String) {
            _password.value = value
        }

        fun toggleDebugMode() {
            _isDebugMode.value = !_isDebugMode.value
            Log.d("LoginViewModel", "Debug mode toggled. New value: ${_isDebugMode.value}")
        }
    }
