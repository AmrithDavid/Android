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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.TextFieldTextColor
import com.singularhealth.android3dicom.ui.theme.TitleColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val buttonCornerRadius = 8

    val context = LocalContext.current
    val view = LocalView.current
    val statusBarColor = MaterialTheme.colorScheme.primary
    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as? android.app.Activity)?.window
            window?.statusBarColor = statusBarColor.toArgb()
            window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
            }
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top section with support icon and text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarHeight + 10.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_support),
                contentDescription = "Support",
                tint = DarkBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Support",
                style = MaterialTheme.typography.titleMedium,
                color = DarkBlue
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 63.dp, start = 16.dp, end = 16.dp), // 73dp - 10dp (support row height)
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = "3Dicom Logo",
                modifier = Modifier
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
                label = { Text("Email", style = MaterialTheme.typography.bodyLarge, color = TextFieldTextColor) },
                singleLine = true,
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextFieldTextColor),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = TextFieldTextColor,
                    focusedBorderColor = TextFieldTextColor,
                    unfocusedTextColor = TextFieldTextColor,
                    focusedTextColor = TextFieldTextColor,
                    cursorColor = TextFieldTextColor,
                ),
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", style = MaterialTheme.typography.bodyLarge, color = TextFieldTextColor) },
                singleLine = true,
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextFieldTextColor),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visiblity),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = TextFieldTextColor,
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = TextFieldTextColor,
                    focusedBorderColor = TextFieldTextColor,
                    unfocusedTextColor = TextFieldTextColor,
                    focusedTextColor = TextFieldTextColor,
                    cursorColor = TextFieldTextColor,
                ),
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { /* Handle login */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue.copy(alpha = 0.5f),
                ),
                shape = RoundedCornerShape(buttonCornerRadius.dp),
            ) {
                Text("Log in", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Forgot your password?",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle forgot password */ },
            )

            Spacer(modifier = Modifier.height(45.dp))

            Row(
                modifier = Modifier.width(300.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    color = SubheadingColor,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Or Log in with",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubheadingColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(
                    color = SubheadingColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Bottom bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFFF0F3F5)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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


    @Suppress("ktlint:standard:function-naming")
    @Preview(showBackground = true)
    @Composable
    fun LoginScreenPreview() {
        Android3DicomTheme {
            LoginScreen()
        }
    }

