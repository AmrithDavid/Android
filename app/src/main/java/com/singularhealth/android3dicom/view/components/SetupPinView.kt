@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.ErrorState
import com.singularhealth.android3dicom.model.PinState
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.view.components.PinInputVisual
import com.singularhealth.android3dicom.viewmodel.PinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinSetupScreen(
    viewModel: PinViewModel = hiltViewModel(),
    onSetupSuccess: () -> Unit,
) {
    val firstPin by viewModel.firstPin.collectAsStateWithLifecycle()
    val secondPin by viewModel.secondPin.collectAsStateWithLifecycle()
    val pinState by viewModel.pinState.collectAsStateWithLifecycle()

    val isPinComplete = firstPin.length == 4 && secondPin.length == 4

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        SetupPinTopBar()

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
                isError = pinState is PinState.Error,
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Confirm your PIN",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(16.dp))

            PinInputVisual(
                pin = secondPin,
                onPinChange = viewModel::updateSecondPin,
                isError = pinState is PinState.Error,
            )

            if (pinState is PinState.Error) {
                Text(
                    text =
                        when ((pinState as PinState.Error).errorState) {
                            ErrorState.PinTooShort -> "PIN must be 4 digits"
                            ErrorState.PinsDoNotMatch -> "PINs do not match"
                            else -> "An error occurred"
                        },
                    color = Color.Red,
                )
            }

            Spacer(modifier = Modifier.height(76.dp))

            Button(
                onClick = {
                    if (isPinComplete) {
                        viewModel.savePinAndUpdatePreference()
                    }
                },
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (isPinComplete) DarkBlue else DarkBlue.copy(alpha = 0.5f),
                        disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
                    ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedButton(
                onClick = { viewModel.clearPins() },
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
fun SetupPinTopBar() {
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
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Hello Jacob",
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

/* @Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PinSetupScreenPreview() {
    MaterialTheme {
        PinSetupScreen()
    }
} */
