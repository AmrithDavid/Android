@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.ErrorState
import com.singularhealth.android3dicom.model.PinState
import com.singularhealth.android3dicom.ui.theme.*
import com.singularhealth.android3dicom.viewmodel.PinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinVerificationScreen(
    viewModel: PinViewModel = hiltViewModel(),
    onVerificationSuccess: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    val pinState by viewModel.pinState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val view = LocalView.current

    SideEffect {
        val window = (context as? android.app.Activity)?.window
        window?.statusBarColor = Color.White.toArgb()
        WindowCompat.getInsetsController(window!!, view).isAppearanceLightStatusBars = true
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
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(63.dp))

            Image(
                painter = painterResource(id = R.drawable.img_logo_no_title),
                contentDescription = "3Dicom Logo",
                modifier =
                    Modifier
                        .size(width = 270.dp, height = 110.dp)
                        .padding(bottom = 29.dp),
            )

            Text(
                text = "Hello Jacob",
                style = MaterialTheme.typography.headlineMedium,
                color = TitleColor,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = "Enter your pin",
                style = MaterialTheme.typography.headlineMedium,
                color = TitleColor,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Spacer(modifier = Modifier.height(30.dp))

            // PIN input circles
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp),
            ) {
                repeat(4) { index ->
                    Box(
                        modifier =
                            Modifier
                                .size(26.dp)
                                .background(
                                    if (index < pin.length) DarkBlue else Color.Transparent,
                                    CircleShape,
                                ).border(2.dp, BorderColor, CircleShape),
                    )
                }
            }

            when (pinState) {
                is PinState.Loading -> CircularProgressIndicator(color = DarkBlue)
                is PinState.Error -> {
                    Text(
                        text =
                            when ((pinState as PinState.Error).errorState) {
                                ErrorState.IncorrectPin -> "Incorrect PIN"
                                ErrorState.PinTooShort -> "PIN must be 4 digits"
                                ErrorState.PinsDoNotMatch -> "PINs do not match"
                                ErrorState.SaveFailed -> "Failed to save PIN"
                                ErrorState.None -> "An error occurred"
                            },
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
                is PinState.Success -> LaunchedEffect(Unit) { onVerificationSuccess() }
                else -> { /* Initial or Valid state, do nothing */ }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Number pad
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "backspace"),
                ).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        row.forEach { digit ->
                            if (digit == "") {
                                Spacer(modifier = Modifier.size(90.dp, 56.dp))
                            } else {
                                Box(
                                    modifier =
                                        Modifier
                                            .size(90.dp, 56.dp)
                                            .clickable {
                                                when (digit) {
                                                    "backspace" ->
                                                        if (pin.isNotEmpty()) {
                                                            pin = pin.dropLast(1)
                                                        }
                                                    else -> if (pin.length < 4) pin += digit
                                                }
                                                if (pin.length == 4) {
                                                    viewModel.verifyPin(pin)
                                                }
                                            },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    when (digit) {
                                        "backspace" ->
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_backspace),
                                                contentDescription = "Backspace",
                                                tint = TitleColor,
                                                modifier = Modifier.size(36.dp),
                                            )
                                        else ->
                                            Text(
                                                text = digit,
                                                style =
                                                    MaterialTheme.typography.headlineLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = TitleColor,
                                                    ),
                                                fontSize = 32.sp,
                                            )
                                    }
                                }
                            }
                        }
                    }
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
                text = "Use a different account",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .clickable { /* Handle different account */ },
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier =
                    Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(Color.Gray.copy(alpha = 0.5f)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Forgotten your PIN?",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier
                        .padding(end = 16.dp)
                        .clickable { /* Handle forgotten PIN */ },
            )
        }
    }
}
