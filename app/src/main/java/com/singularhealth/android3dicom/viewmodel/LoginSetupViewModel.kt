package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSetupViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        private val _loginPreference = MutableStateFlow<LoginPreferenceOption>(LoginPreferenceOption.NONE)
        val loginPreference: StateFlow<LoginPreferenceOption> = _loginPreference.asStateFlow()

        private val _navigateToPinSetup = MutableStateFlow(false)
        val navigateToPinSetup: StateFlow<Boolean> = _navigateToPinSetup.asStateFlow()

        private val _navigateToBiometricSetup = MutableStateFlow(false)
        val navigateToBiometricSetup: StateFlow<Boolean> = _navigateToBiometricSetup.asStateFlow()

        private val _navigateToLogin = MutableStateFlow(false)
        val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        init {
            viewModelScope.launch {
                _loginPreference.value = appState.loginPreference
            }
        }

        fun saveLoginPreference(preference: LoginPreferenceOption) {
            viewModelScope.launch {
                appState.loginPreference = preference
                _loginPreference.value = preference
            }
        }

        fun onSetupClick() {
            when (_loginPreference.value) {
                LoginPreferenceOption.PIN -> _navigateToPinSetup.value = true
                LoginPreferenceOption.BIOMETRIC -> {
                    // Biometric authentication is triggered in the View
                    // The result will be handled by onBiometricAuthSuccess or onBiometricAuthError
                }
                LoginPreferenceOption.NONE -> {
                    _errorMessage.value = "Please select a login method"
                }
            }
        }

        fun onBiometricAuthSuccess() {
            viewModelScope.launch {
                appState.loginPreference = LoginPreferenceOption.BIOMETRIC
                _navigateToBiometricSetup.value = true
            }
        }

        fun onBiometricAuthError(
            errorCode: Int,
            errorString: String,
        ) {
            _errorMessage.value = "Biometric authentication failed: $errorString"
            // You might want to handle specific error codes differently
            when (errorCode) {
                // Add specific error handling if needed
            }
        }

        fun onNavigatedToPinSetup() {
            _navigateToPinSetup.value = false
        }

        fun onNavigatedToBiometricSetup() {
            _navigateToBiometricSetup.value = false
        }

        fun onCancelClick() {
            _navigateToLogin.value = true
        }

        fun onNavigatedToLogin() {
            _navigateToLogin.value = false
        }

        fun clearErrorMessage() {
            _errorMessage.value = null
        }
    }
