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
fun LogoutConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.size(width = 280.dp, height = 241.dp),
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
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Logout",
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = "Log out?",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "Your email and password will need to be re-entered before you can use the app again.",
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
                        Text("Log out", color = LightBlue)
                    }
                }
            }
        }
    }
}
