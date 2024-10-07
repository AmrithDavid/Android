@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.LightBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun ClearCacheDialog(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.size(width = 280.dp, height = 340.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cache),
                    contentDescription = "Clear cache",
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = "Clear cache?",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = @Suppress("ktlint:standard:max-line-length")
                    "Clearing your cache will delete all downloaded scans on your device. You may lose access if you do not have cloud storage enabled. To ensure your scans are stored securely on the cloud for download in future, please visit the 3Dicom web portal.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel", color = SubheadingColor)
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                    TextButton(onClick = onConfirmLogout) {
                        Text("Clear Cache", color = LightBlue)
                    }
                }
            }
        }
    }
}
