@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
import com.singularhealth.android3dicom.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current)),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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
            Icon(
                painter = painterResource(id = R.drawable.ic_support),
                contentDescription = "Support",
                tint = DarkBlue,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Support",
                style = MaterialTheme.typography.titleMedium,
                color = DarkBlue,
            )
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
                onValueChange = { email = it },
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
                        focusedBorderColor = LightBlue,
                        unfocusedBorderColor = if (email.isBlank()) TextFieldTextColor else TitleColor,
                        focusedLabelColor = LightBlue,
                        unfocusedLabelColor = if (email.isBlank()) TextFieldTextColor else TitleColor,
                    ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .onFocusChanged { isPasswordFocused = it.isFocused },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint =
                                when {
                                    isPasswordFocused -> TitleColor
                                    password.isNotBlank() -> TitleColor
                                    else -> TextFieldTextColor
                                },
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (password.isBlank()) TextFieldTextColor else TitleColor,
                        focusedBorderColor = LightBlue,
                        unfocusedBorderColor = if (password.isBlank()) TextFieldTextColor else TitleColor,
                        focusedLabelColor = LightBlue,
                        unfocusedLabelColor = if (password.isBlank()) TextFieldTextColor else TitleColor,
                    ),
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    errorMessage = null
                    isLoading = true
                    scope.launch {
                        val success = viewModel.loginUser(email, password)
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Login failed"
                        }
                    }
                },
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
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                    )
                } else {
                    Text(
                        "Log in",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Forgot your password?",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle forgot password */ },
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage!!,
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
                modifier = Modifier.clickable { /* Handle sign up */ },
            )
        }
    }
}

/* @Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    Android3DicomTheme {
        LoginScreen(
            onLoginSuccess = {},
        )
    }
} */
