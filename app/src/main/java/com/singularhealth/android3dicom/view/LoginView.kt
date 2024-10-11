@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

// required for
import android.content.Intent
import android.net.Uri

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
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
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_support),
                            contentDescription = "Support",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Visit the customer support website?",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            modifier =
                                Modifier
                                    .width(232.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                            textAlign = TextAlign.Center, // Center text inside the pop-up
                        )
                    }
                },
                text = {
                    Text(
                        "Got a question or need some help? We are here to help.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier =
                            Modifier
                                .width(232.dp)
                                .height(36.dp)
                                .fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        lineHeight = 15.sp,
                    )
                },
                confirmButton = {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .width(144.dp)
                                .height(36.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text(
                                "Cancel",
                                color = Color(0xFF606066), // Gray color for Cancel button
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }

                        TextButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://3dicomviewer.com/knowledgebase"))
                                showDialog = false
                                context.startActivity(intent) // Launch the browser with the URL
                            },
                        ) {
                            Text(
                                "OK",
                                color = Color(0xFF50A5DE), // Blue color for OK button
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                },
                properties =
                    DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    ),
                shape = RoundedCornerShape(16.dp), // Rounded corners
                containerColor = Color.White, // Solid white background for the pop-up
                modifier =
                    Modifier
                        .width(280.dp)
                        .height(252.dp),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarHeight + 10.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { showDialog = true }, // Action on click
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF2E3176), // Set text and icon color
                        containerColor = Color.Transparent,
                    ),
                shape = RoundedCornerShape(10.dp),
                modifier =
                    Modifier
                        .padding(4.dp),
            ) {
                // Icon and Text inside the button
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
                        color = Color(0xFF2E3176), // Text color
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
                    email = it
                    hasError = false // Reset error state when user types
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
                onValueChange = {
                    password = it
                    hasError = false // Reset error state when user types
                },
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
                onClick = {
                    isLoading = true
                    scope.launch {
                        val success = viewModel.loginUser(email, password)
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            hasError = true // Set error state
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

            Spacer(modifier = Modifier.height(60.dp))

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
