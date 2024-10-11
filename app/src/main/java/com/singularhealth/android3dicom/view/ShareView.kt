@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.BorderColor
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.viewmodel.ShareViewModel

@Composable
fun ShareView(viewModel: ShareViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val remainingCredits by viewModel.remainingCredits.collectAsState()
    var includeReport by remember { mutableStateOf(false) }
    var hasConsent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    // A regular expression for basic email validation "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
    // val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.+[A-Za-z0-9.-]$".toRegex()
    val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()

    LaunchedEffect(Unit) {
        viewModel.loadAvailableCredits()
    }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ShareTopBar(onBackClick = { viewModel.onBackClick() })
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "Share scan via email",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text =
                    buildAnnotatedString {
                        pushStyle(
                            MaterialTheme.typography.bodyMedium
                                .toSpanStyle()
                                .copy(color = SubheadingColor),
                        )
                        append("Sharing this scan will cost ")
                        pushStyle(
                            MaterialTheme.typography.displayLarge
                                .toSpanStyle()
                                .copy(color = SubheadingColor),
                        )
                        append("${viewModel.SharingCost} credit")
                        pop()
                        append(".")
                    },
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text =
                    buildAnnotatedString {
                        pushStyle(
                            MaterialTheme.typography.bodyMedium
                                .toSpanStyle()
                                .copy(color = SubheadingColor),
                        )
                        append("After sharing you will have ")
                        pushStyle(
                            MaterialTheme.typography.displayLarge
                                .toSpanStyle()
                                .copy(color = SubheadingColor),
                        )
                        append("$remainingCredits credit(s)")
                        pop()
                        append(" remaining.")
                    },
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError =
                            if (!emailRegex.matches(it)) {
                                "Invalid email format"
                            } else {
                                null
                            }
                        viewModel.onEmailChanged(it)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .width(300.dp)
                            .padding(start = 56.dp, end = 56.dp)
                            .border(
                                1.dp,
                                if (emailError == null) BorderColor else Color(0xFFB21717),
                                RoundedCornerShape(4.dp),
                            ),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(
                            "Recipient's Email",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (emailError == null) BorderColor else Color.Transparent,
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    // Use trailingIcon to display the icon inside the text box
                    trailingIcon = {
                        if (emailError != null) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_warning), // Replace with your icon
                                contentDescription = "Warning icon",
                                tint = Color(0xFFB21717),
                            )
                        }
                    },
                )

                Text(
                    text = "Recipient's Email",
                    color = if (emailError == null) Color.Transparent else Color(0xFFB21717),
                    style = MaterialTheme.typography.labelMedium,
                    modifier =
                        Modifier
                            .padding(start = 72.dp)
                            .background(if (emailError == null) Color.Transparent else Color.White)
                            .offset(y = (-7).dp),
                )
            }

            // Display email error message if invalid
            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = Color(0xFFB21717),
                    style = MaterialTheme.typography.labelMedium,
                    modifier =
                        Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                            .offset(x = 72.dp),
                    textAlign = TextAlign.Left,
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = includeReport,
                    onCheckedChange = { includeReport = it },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = SubheadingColor,
                        ),
                    modifier = Modifier.padding(end = 1.dp),
                )
                Text(
                    "Include Report",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubheadingColor,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = hasConsent,
                    onCheckedChange = { hasConsent = it },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = SubheadingColor,
                        ),
                    modifier = Modifier.padding(end = 1.dp),
                )
                Text(
                    "By checking this box I declare that I have full consent and permission to share these files with other parties",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubheadingColor,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
            Spacer(modifier = Modifier.height(56.dp))

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { viewModel.shareScan() },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    enabled = (emailError == null && hasConsent),
                    modifier =
                        Modifier
                            .height(35.dp)
                            .fillMaxWidth(0.75f),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(
                        "Send(1 Credit)",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun ShareTopBar(onBackClick: () -> Unit) {
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
                        .clickable { onBackClick() },
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Share",
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

@Preview(showBackground = true)
@Composable
fun ShareViewPreview() {
    Android3DicomTheme {
        // ShareView(ShareViewModel())
    }
}
