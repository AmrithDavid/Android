package com.singularhealth.android3dicom.view

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// **** EXAMPLE FILE FOR GOOD CODE STRUCTURE ****//
class BiometricViewModel : ViewModel() {
    private val _biometricState = MutableStateFlow<BiometricState>(BiometricState.Initial)
    val biometricState: StateFlow<BiometricState> = _biometricState.asStateFlow()

    fun updateBiometricState(newState: BiometricState) {
        _biometricState.value = newState
    }
}

sealed class BiometricState {
    object Initial : BiometricState()

    object Success : BiometricState()

    data class Error(
        val message: String,
    ) : BiometricState()
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun BiometricAuthScreen(
    viewModel: BiometricViewModel =
        androidx.lifecycle.viewmodel.compose
            .viewModel(),
) {
    val context = LocalContext.current
    val biometricState by viewModel.biometricState.collectAsState()

    val biometricManager = remember { BiometricManager.from(context) }
    val executor = remember { ContextCompat.getMainExecutor(context) }

    val promptInfo =
        remember {
            BiometricPrompt.PromptInfo
                .Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()
        }

    val biometricPrompt =
        remember {
            BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        viewModel.updateBiometricState(BiometricState.Success)
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence,
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        viewModel.updateBiometricState(BiometricState.Error(errString.toString()))
                    }
                },
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
                    else -> viewModel.updateBiometricState(BiometricState.Error("Biometric authentication not available"))
                }
            },
        ) {
            Text("Authenticate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = biometricState) {
            is BiometricState.Success -> Text("Authentication successful!")
            is BiometricState.Error -> Text("Error: ${state.message}")
            else -> {}
        }
    }
}
