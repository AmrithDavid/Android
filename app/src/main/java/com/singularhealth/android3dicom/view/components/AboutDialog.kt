@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Composable
fun AboutDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .size(width = 280.dp, height = 278.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "About",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF1D1D1F)
                )
                Text(
                    text = "About 3Dicom Android Mobile App",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "App version: vX.XX\n\nThe 3Dicom Mobile app is not a medical device.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                    textAlign = TextAlign.Left,
                    lineHeight = 15.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .offset(y=10.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text("OK", color = Color(0xFF50A5DE))
                    }
                }

            }
        }
    }
}
