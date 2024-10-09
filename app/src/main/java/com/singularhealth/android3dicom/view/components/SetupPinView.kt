@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.PinState
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.WarningRed
import com.singularhealth.android3dicom.viewmodel.PinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinSetupScreen(
    viewModel: PinViewModel = hiltViewModel(),
    onSetupSuccess: () -> Unit,
    onCancel: () -> Unit,
    onBackClick: () -> Unit, // New parameter for back navigation
) {
    val firstPin by viewModel.firstPin.collectAsStateWithLifecycle()
    val secondPin by viewModel.secondPin.collectAsStateWithLifecycle()
    val pinState by viewModel.pinState.collectAsStateWithLifecycle()
    val isFirstPinComplete by viewModel.isFirstPinComplete.collectAsStateWithLifecycle()

    val isPinComplete = firstPin.length == 4 && secondPin.length == 4
    val isError = pinState is PinState.Error

    val view = LocalView.current
    SideEffect {
        val window =
            (view.context as? Activity)?.window
                ?: throw Exception("Not in an activity - unable to get Window reference")
        window.statusBarColor = DarkBlue.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        SetupPinTopBar(onBackClick = onBackClick) // Pass the onBackClick function here

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(130.dp))

            Text(
                text = "Set up your pin",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Enter your 4-digit PIN",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(16.dp))

            PinInputVisual(
                pin = firstPin,
                onPinChange = viewModel::updateFirstPin,
                isError = isError,
                enabled = true,
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Confirm your PIN",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                color = if (isFirstPinComplete) SubheadingColor else SubheadingColor.copy(alpha = 0.5f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            PinInputVisual(
                pin = secondPin,
                onPinChange = viewModel::updateSecondPin,
                isError = isError,
                enabled = isFirstPinComplete,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isError) {
                Box(
                    modifier =
                        Modifier
                            .width(300.dp)
                            .height(38.dp)
                            .background(
                                color = WarningRed,
                                shape = RoundedCornerShape(4.dp),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "The PIN values entered do not match",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(38.dp))
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    if (isPinComplete && !isError) {
                        viewModel.savePinAndUpdatePreference()
                    }
                },
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (isPinComplete && !isError) DarkBlue else DarkBlue.copy(alpha = 0.5f),
                        disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
                    ),
                shape = RoundedCornerShape(8.dp),
                enabled = isPinComplete && !isError,
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = DarkBlue,
                    ),
                border = BorderStroke(1.dp, DarkBlue),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                    color = DarkBlue,
                )
            }
        }
    }

    LaunchedEffect(pinState) {
        if (pinState is PinState.Success) {
            onSetupSuccess()
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SetupPinTopBar(onBackClick: () -> Unit) { // Added onBackClick parameter
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
                // Added clickable modifier
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
