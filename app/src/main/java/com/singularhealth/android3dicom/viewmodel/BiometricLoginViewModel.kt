package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BiometricLoginViewModel
    @Inject
    constructor(
        var appState: AppState,
    ) : ViewModel() {
        private val _showSupportDialog = MutableStateFlow(false)
        val showSupportDialog: StateFlow<Boolean> = _showSupportDialog.asStateFlow()

        fun onLoad(context: Context) {
            appState.initialiseBiometricPrompt(context, ::onLoginSuccess, ::onLoginError)
            appState.promptBiometricLogin()
        }

        fun onSupport() {
            _showSupportDialog.value = true
        }

        fun onDismissSupport() {
            _showSupportDialog.value = false
        }

        fun onFingerprint() {
            appState.promptBiometricLogin()
        }

        fun onDifferentAccount() {
            appState.navigateTo(ViewRoute.LOGIN)
        }

        private fun onLoginSuccess() {
            appState.navigateTo(ViewRoute.SCAN_LIBRARY)
        }

        private fun onLoginError(
            errorCode: Int,
            errorMsg: String,
        ) {
            Log.e("BiometricLoginViewModel", "Login failed with error: $errorMsg")
            // Show error toast
        }
    }
