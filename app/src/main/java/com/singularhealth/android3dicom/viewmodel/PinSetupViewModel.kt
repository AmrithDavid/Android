@file:Suppress("ktlint:standard:filename")

package com.singularhealth.android3dicom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.ErrorState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.model.PinState
import com.singularhealth.android3dicom.utilities.KeystorePinHandler
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
    ) : ViewModel() {
        private val _firstPin = MutableStateFlow("")
        val firstPin: StateFlow<String> = _firstPin.asStateFlow()

        private val _secondPin = MutableStateFlow("")
        val secondPin: StateFlow<String> = _secondPin.asStateFlow()

        private val _pinState = MutableStateFlow<PinState>(PinState.Initial)
        val pinState: StateFlow<PinState> = _pinState.asStateFlow()

        private var _username = MutableStateFlow("")
        val username: StateFlow<String> = _username.asStateFlow()

        private val _isFirstPinComplete = MutableStateFlow(false)
        val isFirstPinComplete: StateFlow<Boolean> = _isFirstPinComplete.asStateFlow()

        fun onLoad() {
            appState.setOnUserDataReceivedListener { _username.value = it.firstName }
            _username.value = appState.getCurrentUserName()
        }

        fun updateFirstPin(pin: String) {
            _firstPin.value = pin
            Log.d("PIN_SETUP", "First pin value is: $pin")
            _isFirstPinComplete.value = pin.length == 4
            validatePins()
        }

        fun updateSecondPin(pin: String) {
            _secondPin.value = pin
            Log.d("PIN_SETUP", "Second pin value is: $pin")
            validatePins()
        }

        private fun validatePins() {
            _pinState.value =
                when {
                    _firstPin.value.length == 4 &&
                        _secondPin.value.length == 4 &&
                        _firstPin.value != _secondPin.value ->
                        PinState.Error(ErrorState.PinsDoNotMatch)
                    else -> PinState.Initial
                }
        }

        fun savePinAndUpdatePreference() {
            viewModelScope.launch {
                _pinState.value = PinState.Loading
                try {
                    KeystorePinHandler.setPin(_firstPin.value)
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
            _isFirstPinComplete.value = false
            _pinState.value = PinState.Initial
        }

        fun verifyPin(enteredPin: String) {
            viewModelScope.launch {
                _pinState.value = PinState.Loading
                val isPinCorrect = KeystorePinHandler.verifyPin(enteredPin)
                _pinState.value =
                    if (isPinCorrect) {
                        PinState.Success
                    } else {
                        PinState.Error(ErrorState.IncorrectPin)
                    }
            }
        }
    }
