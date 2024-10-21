package com.singularhealth.android3dicom.viewmodel

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
        fun onLoad() {
            // Trigger the biometric login prompt here
            appState.promptBiometricLogin()
        }

        fun onSupport() {
            // Show support dialogue; probably toggle boolean
        }

        fun onDifferentAccount() {
            appState.navigateTo(ViewRoute.LOGIN)
        }
    }
