package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import javax.inject.Inject

class ShareViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        private val _email = MutableLiveData<String>()
        val email: LiveData<String> get() = _email

        fun onEmailChanged(newEmail: String) {
            _email.value = newEmail
        }

        fun getAvailableCredits(): Int = appState.getAvailableCredits()

        fun shareScan() {
            email.value?.let { appState.shareScan(it) }
        }

        fun onBackClick() {
            appState.navigateBack()
        }
    }
