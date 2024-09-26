@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.*

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginSetupView(onBackClick: () -> Unit = {}) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        LoginTopBar(onBackClick)
        LoginContent(
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
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
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
) {
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
            style = MaterialTheme.typography.displayLarge,
            color = TitleColor,
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
                isSelected = selectedOption == "Biometric",
                onSelect = { onOptionSelected("Biometric") },
                modifier = Modifier.weight(1f),
            )
            LoginOptionItem(
                text = "PIN",
                description = "Log in by setting up a 4-digit PIN",
                icon = R.drawable.ic_pin,
                isSelected = selectedOption == "PIN",
                onSelect = { onOptionSelected("PIN") },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = { /* TODO: Handle setup */ },
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
    val borderColor = if (isSelected) SelectedOptionBackground else ButtonBorderColor
    val circleSize = 18.dp
    val circleBorderWidth = 1.5.dp
    val circlePadding = 1.5.dp

    Box(
        modifier =
            modifier
                .height(130.dp)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
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
                modifier = Modifier.size(24.dp),
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
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(SelectedOptionCircle),
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun LoginSetupViewPreview() {
    Android3DicomTheme {
        LoginSetupView()
    }
}
