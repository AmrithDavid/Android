@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.BackgroundGray
import com.singularhealth.android3dicom.ui.theme.BorderColor
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinSetupScreen() {
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

            PinInputVisual()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Confirm your PIN",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )
            Spacer(modifier = Modifier.height(16.dp))

            PinInputVisual()

            Spacer(modifier = Modifier.height(76.dp))

            Button(
                onClick = { /* TODO: Handle confirm click */ },
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = DarkBlue.copy(alpha = 0.5f),
                    ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedButton(
                onClick = { /* TODO: Handle cancel click */ },
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(40.dp),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = DarkBlue,
                    ),
                border = BorderStroke(1.dp, DarkBlue), // border parameter of outlined button which overrides default modifier border
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                    color = DarkBlue,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinInputVisual() {
    Box(
        modifier =
            Modifier
                .width(200.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundGray),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            repeat(4) {
                Box(
                    modifier =
                        Modifier
                            .size(26.dp)
                            .border(2.dp, BorderColor, CircleShape)
                            .clip(CircleShape),
                )
            }
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

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PinSetupScreenPreview() {
    MaterialTheme {
        PinSetupScreen()
    }
}
