@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.view.components.SupportDialog
import com.singularhealth.android3dicom.viewmodel.BiometricLoginViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun BiometricLoginView(viewModel: BiometricLoginViewModel = hiltViewModel()) {
    val showSupportDialog by viewModel.showSupportDialog.collectAsStateWithLifecycle()
    var context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onLoad(context)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

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
                modifier = Modifier.clickable { viewModel.onSupport() },
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
                text = "Hello Jacob",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Use Your Fingerprint",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(90.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_fingerprint_biometric),
                contentDescription = "Fingerprint",
                modifier =
                    Modifier
                        .size(150.dp)
                        .clickable { viewModel.onFingerprint() },
            )
        }

        // Bottom bar
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF0F3F5)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Use a different account",
                style = MaterialTheme.typography.labelLarge,
                color = DarkBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { viewModel.onDifferentAccount() },
            )
        }
    }

    if (showSupportDialog) {
        SupportDialog(
            onDismissRequest = { viewModel.onDismissSupport() },
            context = LocalContext.current,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BiometricLoginViewPreview() {
    MaterialTheme {
        BiometricLoginView()
    }
}
