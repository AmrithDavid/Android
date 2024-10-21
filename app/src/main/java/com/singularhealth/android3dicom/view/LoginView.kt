@file:Suppress("ktlint:standard:import-ordering", "ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.view.components.SupportDialog
import com.singularhealth.android3dicom.viewmodel.LoginViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val hasError by viewModel.hasError.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }

    val buttonCornerRadius = 8

    val context = LocalContext.current
    val view = LocalView.current

    SideEffect {
        val window = (context as? android.app.Activity)?.window
        window?.statusBarColor = Color.White.toArgb()
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = true
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        // Top section with support icon and text
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarHeight + 10.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { showSupportDialog = true },
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF2E3176),
                        containerColor = Color.Transparent,
                    ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(4.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_support),
                        contentDescription = "Support",
                        tint = Color(0xFF2E3176),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Support",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E3176),
                    )
                }
            }
        }

        // Main content
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 63.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_no_title),
                contentDescription = "3Dicom Logo",
                modifier =
                    Modifier
                        .size(width = 270.dp, height = 110.dp)
                        .padding(bottom = 29.dp),
            )

            Text(
                text = "Log in using your 3Dicom account",
                style = MaterialTheme.typography.titleLarge,
                color = TitleColor,
                modifier = Modifier.padding(bottom = 23.dp),
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    viewModel.onEmailChanged(it)
                    viewModel.resetErrorState()
                },
                label = { Text("Email") },
                singleLine = true,
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .onFocusChanged { isEmailFocused = it.isFocused },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (email.isBlank()) TextFieldTextColor else TitleColor,
                        focusedBorderColor = if (hasError) ErrorRed else LightBlue,
                        unfocusedBorderColor =
                            when {
                                hasError -> ErrorRed
                                email.isBlank() -> TextFieldTextColor
                                else -> TitleColor
                            },
                        focusedLabelColor = if (hasError) ErrorRed else LightBlue,
                        unfocusedLabelColor =
                            when {
                                hasError -> ErrorRed
                                email.isBlank() -> TextFieldTextColor
                                else -> TitleColor
                            },
                    ),
                trailingIcon = {
                    if (hasError) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_warning),
                            contentDescription = "Error",
                            tint = ErrorRed,
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                singleLine = true,
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .onFocusChanged { isPasswordFocused = it.isFocused },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (password.isBlank()) TextFieldTextColor else TitleColor,
                        focusedBorderColor = if (hasError) ErrorRed else LightBlue,
                        unfocusedBorderColor =
                            when {
                                hasError -> ErrorRed
                                password.isBlank() -> TextFieldTextColor
                                else -> TitleColor
                            },
                        focusedLabelColor = if (hasError) ErrorRed else LightBlue,
                        unfocusedLabelColor =
                            when {
                                hasError -> ErrorRed
                                password.isBlank() -> TextFieldTextColor
                                else -> TitleColor
                            },
                    ),
                trailingIcon = {
                    if (hasError) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_warning),
                            contentDescription = "Error",
                            tint = ErrorRed,
                        )
                    } else {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter =
                                    painterResource(
                                        id = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
                                    ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint =
                                    when {
                                        isPasswordFocused -> TitleColor
                                        password.isNotBlank() -> TitleColor
                                        else -> TextFieldTextColor
                                    },
                            )
                        }
                    }
                },
            )

            // Fixed space of 60dp with error message if needed
            Box(modifier = Modifier.height(80.dp)) {
                if (hasError) {
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.8f)
                                .height(38.dp)
                                .background(
                                    color = WarningRed,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Email or password is incorrect",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.onLoginPressed() },
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (email.isNotBlank() && password.isNotBlank()) DarkBlue else DarkBlue.copy(alpha = 0.5f),
                        disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
                    ),
                shape = RoundedCornerShape(buttonCornerRadius.dp),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            ) {
                Text(
                    "Log in",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Forgot your password?",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle forgot password */ },
            )

            if (errorMessage != "") {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(45.dp))

            Row(
                modifier = Modifier.width(300.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(
                    color = SubheadingColor,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "Or Log in with",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubheadingColor,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                HorizontalDivider(
                    color = SubheadingColor,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .offset(x = 47.dp, y = (-30).dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.microsoft_logo),
                        contentDescription = "Sign in with Microsoft",
                        modifier =
                            Modifier
                                .clickable { /* Trigger Microsoft Sign-In */ }
                                .size(38.dp),
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.apple_logo),
                    contentDescription = "Sign in with Apple",
                    modifier =
                        Modifier
                            .offset(x = 15.dp, y = (-28).dp)
                            .clickable { /* Trigger Apple Sign-In */ }
                            .size(120.dp),
                )

                Box(
                    modifier =
                        Modifier
                            .offset(x = (-30).dp, y = (-30).dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Sign in with Google",
                        modifier =
                            Modifier
                                .clickable { /* Trigger Google Sign-In */ }
                                .size(70.dp),
                    )
                }
            }
        }

        // Bottom bar
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF0F3F5)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = SubheadingColor,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign up now",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://3dicomviewer.com/pricing/"))
                        context.startActivity(intent) // Launch the browser with the URL
                    },
            )
        }
    }

    if (showSupportDialog) {
        SupportDialog(
            onDismissRequest = { showSupportDialog = false },
            context = LocalContext.current,
        )
    }
}
