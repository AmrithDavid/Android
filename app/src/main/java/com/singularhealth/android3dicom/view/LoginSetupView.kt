@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.utilities.BiometricAuthListener
import com.singularhealth.android3dicom.utilities.BiometricUtils
import com.singularhealth.android3dicom.viewmodel.LoginSetupViewModel

private lateinit var biometricPrompt: BiometricPrompt
private lateinit var promptInfo: BiometricPrompt.PromptInfo

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginSetupView(
    viewModel: LoginSetupViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onBiometricLoginClick: () -> Unit,
    onSetupSuccess: () -> Unit,
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Execute the provided initialization function
    InitBiometrics(context as FragmentActivity, onSetupSuccess)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        LoginTopBar(onBackClick)
        LoginContent(
            viewModel,
            selectedOption = selectedOption,
            onOptionSelected = {
                selectedOption = it.toString()
                viewModel.saveLoginPreference(it)
            },
            onSetupClick = {
                if (selectedOption == LoginPreferenceOption.BIOMETRIC.toString()) {
                    onBiometricLoginClick()
                } else {
                    // Handle PIN setup
                    // TODO: Implement PIN setup
                }
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun LoginTopBar(onBackClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(DarkBlue),
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.White,
                modifier =
                    Modifier
                        .size(24.dp)
                        .clickable(onClick = onBackClick),
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Hello Sam",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.img_logomark),
                contentDescription = "Logo",
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun LoginContent(
    viewModel: LoginSetupViewModel,
    selectedOption: String?,
    onOptionSelected: (LoginPreferenceOption) -> Unit,
    onSetupClick: () -> Unit,
) {
    val typography = MaterialTheme.typography

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Text(
            text = "How would you like to log in?",
            style = typography.displayLarge.copy(fontSize = 24.sp),
            color = TitleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(56.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LoginOptionItem(
                text = "Biometric",
                description = "Log in using your fingerprint or face ID",
                icon = R.drawable.ic_fingerprint,
                isSelected = selectedOption == LoginPreferenceOption.BIOMETRIC.toString(),
                onSelect = { onOptionSelected(LoginPreferenceOption.BIOMETRIC) },
                modifier = Modifier.weight(1f),
            )
            LoginOptionItem(
                text = "PIN",
                description = "Log in by setting up a 4-digit PIN",
                icon = R.drawable.ic_pin,
                isSelected = selectedOption == LoginPreferenceOption.PIN.toString(),
                onSelect = { onOptionSelected(LoginPreferenceOption.PIN) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
                Log.d("LoginSetupView", "The selected login type is: ${viewModel.getLoginPreference()}")
                onSetupClick()
            },
            modifier =
                Modifier
                    .width(300.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    disabledContainerColor = if (selectedOption != null) DarkBlue else DarkBlue.copy(alpha = 0.5f),
                ),
            shape = RoundedCornerShape(4.dp),
            enabled = selectedOption != null,
        ) {
            Text(
                "Setup",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedButton(
            onClick = { /* TODO: Handle cancel */ },
            modifier =
                Modifier
                    .width(300.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                ),
            border = BorderStroke(2.dp, DarkBlue),
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(
                "Cancel",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun LoginOptionItem(
    text: String,
    description: String,
    icon: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) SelectedOptionBackground else Color.White
    val textColor = if (isSelected) DarkBlue else TitleColor
    val borderColor = if (isSelected) DarkBlue else ButtonBorderColor
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val circleSize = 18.dp
    val circleBorderWidth = 1.5.dp
    val circlePadding = 1.5.dp
    val innerCircleSize = 10.dp

    Box(
        modifier =
            modifier
                .height(130.dp)
                .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .clickable(onClick = onSelect),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = SubheadingColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(circleSize)
                    .border(circleBorderWidth, SubheadingColor, CircleShape)
                    .padding(circlePadding),
        ) {
            if (isSelected) {
                Box(
                    modifier =
                        Modifier
                            .size(innerCircleSize)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(SelectedOptionCircle),
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun InitBiometrics(
    context: FragmentActivity,
    successListener: () -> Unit,
) {
    val callback = makeBiometricCallbacks(successListener)
    biometricPrompt = BiometricUtils.initBiometricPrompt(context, callback, successListener)

    promptInfo =
        BiometricUtils.createPromptInfo(
            title = "Biometric Example",
            description = "Touch your Fingerprint sensor",
            negativeText = "Cancel",
        )
}

private fun makeBiometricCallbacks(onSuccess: () -> Unit): BiometricAuthListener =
    object : BiometricAuthListener {
        override fun onBiometricAuthenticateError(
            error: Int,
            errMsg: String,
        ) {
            when (error) {
                BiometricPrompt.ERROR_USER_CANCELED -> {}
                BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {}
                BiometricPrompt.ERROR_NO_BIOMETRICS -> {}
            }
        }

        override fun onAuthenticationFailed() {}

        override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
            onSuccess()
        }
    }

@Preview(showBackground = true)
@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginSetupViewPreview() {
    Android3DicomTheme {
        LoginSetupView(
            onBackClick = {},
            onBiometricLoginClick = {},
            onSetupSuccess = {},
        )
    }
}
