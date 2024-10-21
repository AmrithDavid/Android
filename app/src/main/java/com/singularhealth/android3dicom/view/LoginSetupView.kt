@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import android.app.Activity
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.viewmodel.LoginSetupViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginSetupView(
    viewModel: LoginSetupViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToPinSetup: () -> Unit,
    onNavigateToBiometricSetup: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val loginPreference by viewModel.loginPreference.collectAsStateWithLifecycle()
    val navigateToPinSetup by viewModel.navigateToPinSetup.collectAsStateWithLifecycle()
    val navigateToBiometricSetup by viewModel.navigateToBiometricSetup.collectAsStateWithLifecycle()
    val navigateToLogin by viewModel.navigateToLogin.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    val context = LocalContext.current as FragmentActivity
    val view = LocalView.current

    SideEffect {
        val window =
            (view.context as? Activity)?.window
                ?: throw Exception("Not in an activity - unable to get Window reference")
        window.statusBarColor = DarkBlue.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        viewModel.onLoad(context)
    }

    LaunchedEffect(navigateToPinSetup) {
        if (navigateToPinSetup) {
            onNavigateToPinSetup()
            viewModel.onNavigatedToPinSetup()
        }
    }

    LaunchedEffect(navigateToBiometricSetup) {
        if (navigateToBiometricSetup) {
            onNavigateToBiometricSetup()
            viewModel.onNavigatedToBiometricSetup()
        }
    }

    LaunchedEffect(navigateToLogin) {
        if (navigateToLogin) {
            onNavigateToLogin()
            viewModel.onNavigatedToLogin()
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        LoginTopBar(onBackClick)
        LoginContent(
            loginPreference = loginPreference,
            onOptionSelected = viewModel::saveLoginPreference,
            onSetupClick = viewModel::onSetupClick,
            onCancelClick = viewModel::onCancelClick,
        )
    }

    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { viewModel.clearErrorMessage() },
            title = { Text("Error") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearErrorMessage() }) {
                    Text("OK")
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
    loginPreference: LoginPreferenceOption,
    onOptionSelected: (LoginPreferenceOption) -> Unit,
    onSetupClick: () -> Unit,
    onCancelClick: () -> Unit,
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
                isSelected = loginPreference == LoginPreferenceOption.BIOMETRIC,
                onSelect = { onOptionSelected(LoginPreferenceOption.BIOMETRIC) },
                modifier = Modifier.weight(1f),
            )
            LoginOptionItem(
                text = "PIN",
                description = "Log in by setting up a 4-digit PIN",
                icon = R.drawable.ic_pin,
                isSelected = loginPreference == LoginPreferenceOption.PIN,
                onSelect = { onOptionSelected(LoginPreferenceOption.PIN) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = onSetupClick,
            modifier =
                Modifier
                    .width(300.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
                ),
            shape = RoundedCornerShape(8.dp),
            enabled = loginPreference != LoginPreferenceOption.NONE,
        ) {
            Text(
                "Setup",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedButton(
            onClick = onCancelClick,
            modifier =
                Modifier
                    .width(300.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                ),
            border = BorderStroke(2.dp, DarkBlue),
            shape = RoundedCornerShape(8.dp),
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

@Preview(showBackground = true)
@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginSetupViewPreview() {
    Android3DicomTheme {
        LoginSetupView(
            viewModel = hiltViewModel(),
            onBackClick = {},
            onNavigateToPinSetup = {},
            onNavigateToBiometricSetup = {},
            onNavigateToLogin = {},
        )
    }
}
