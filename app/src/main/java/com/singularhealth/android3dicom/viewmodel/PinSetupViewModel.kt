@file:Suppress("ktlint:standard:filename")

package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.DataStoreRepository
import com.singularhealth.android3dicom.model.ErrorState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.model.PinState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel
    @Inject
    constructor(
        private val appState: AppState,
        private val dataStoreRepository: DataStoreRepository,
    ) : ViewModel() {
        private val _firstPin = MutableStateFlow("")
        val firstPin: StateFlow<String> = _firstPin.asStateFlow()

        private val _secondPin = MutableStateFlow("")
        val secondPin: StateFlow<String> = _secondPin.asStateFlow()

        private val _pinState = MutableStateFlow<PinState>(PinState.Initial)
        val pinState: StateFlow<PinState> = _pinState.asStateFlow()

        fun updateFirstPin(pin: String) {
            _firstPin.value = pin
            validatePins()
        }

        fun updateSecondPin(pin: String) {
            _secondPin.value = pin
            validatePins()
        }

        private fun validatePins() {
            _pinState.value =
                when {
                    firstPin.value.length < 4 || secondPin.value.length < 4 -> PinState.Error(ErrorState.PinTooShort)
                    firstPin.value != secondPin.value -> PinState.Error(ErrorState.PinsDoNotMatch)
                    else -> PinState.Valid
                }
        }

        fun savePinAndUpdatePreference() {
            viewModelScope.launch {
                _pinState.value = PinState.Loading
                try {
                    dataStoreRepository.setString("USER_PIN", firstPin.value)
                    appState.loginPreference = LoginPreferenceOption.PIN
                    _pinState.value = PinState.Success
                } catch (e: Exception) {
                    _pinState.value = PinState.Error(ErrorState.SaveFailed)
                }
            }
        }

        fun clearPins() {
            _firstPin.value = ""
            _secondPin.value = ""
            _pinState.value = PinState.Initial
        }

        fun verifyPin(enteredPin: String) {
            viewModelScope.launch {
                _pinState.value = PinState.Loading
                val storedPin = dataStoreRepository.getString("USER_PIN")
                _pinState.value =
                    if (enteredPin == storedPin) {
                        PinState.Success
                    } else {
                        PinState.Error(ErrorState.IncorrectPin)
                    }
            }
        }
    }
