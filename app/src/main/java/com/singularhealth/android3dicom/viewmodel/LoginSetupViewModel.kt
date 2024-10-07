package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginSetupViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        fun saveLoginPreference(preference: LoginPreferenceOption) {
            appState.loginPreference = preference
        }

        fun getLoginPreference(): LoginPreferenceOption = appState.loginPreference
    }
