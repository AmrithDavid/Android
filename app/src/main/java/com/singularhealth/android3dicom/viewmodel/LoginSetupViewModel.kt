package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.view.ViewRoute
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

        private val ignoredErrors =
            listOf(
                "You haven't set up Face Unlock",
            )

        init {
            viewModelScope.launch {
                _loginPreference.value = appState.loginPreference
            }
        }

        fun onLoad(context: Context) {
            // Initialise must be called before invoking biometric prompt
            appState.initialiseBiometricPrompt(
                context,
                ::onBiometricAuthSuccess,
                ::onBiometricAuthError,
            )
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
                LoginPreferenceOption.BIOMETRIC -> appState.promptBiometricLogin()
                LoginPreferenceOption.NONE -> {
                    _errorMessage.value = "Please select a login method"
                }
            }
        }

        private fun onBiometricAuthSuccess() {
            appState.loginPreference = LoginPreferenceOption.BIOMETRIC
            appState.navigateTo(ViewRoute.BIOMETRIC_LOGIN)
        }

        private fun onBiometricAuthError(
            errorCode: Int,
            errorString: String,
        ) {
            val errMsg = "Biometric authentication failed. Code is: $errorCode and message is: $errorString"
            Log.e("LoginSetupViewmodel", errMsg)
            _errorMessage.value = errMsg
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
