package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiometricLoginViewModel
    @Inject
    constructor(
        var appState: AppState,
    ) : ViewModel() {
        fun onLoad(context: Context) {
            appState.initialiseBiometricPrompt(context, ::onLoginSuccess, ::onLoginError)
            appState.promptBiometricLogin()
        }

        fun onSupport() {
            // Show support dialogue; probably toggle boolean
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
