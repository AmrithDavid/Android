package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShareViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        val SharingCost = 1

        private val _email = MutableLiveData<String>()
        val email: LiveData<String> get() = _email

        private val _remainingCredits = MutableStateFlow(0)
        val remainingCredits: MutableStateFlow<Int> get() = _remainingCredits

        fun onEmailChanged(newEmail: String) {
            _email.value = newEmail
        }

        fun loadAvailableCredits() {
            var availableCredits = appState.getAvailableCredits() ?: 0
            _remainingCredits.value = availableCredits - SharingCost
        }

        fun shareScan() {
            email.value?.let { appState.shareScan(it) }
        }

        fun onBackClick() {
            appState.navigateBack()
        }
    }
