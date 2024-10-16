@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.singularhealth.android3dicom.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupportDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_support),
                        contentDescription = "Support",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Visit the customer support website?",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier =
                            Modifier
                                .width(232.dp)
                                .height(50.dp)
                                .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            },
            text = {
                Text(
                    "Got a question or need some help? We are here to help.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier =
                        Modifier
                            .width(232.dp)
                            .height(36.dp)
                            .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    lineHeight = 15.sp,
                )
            },
            confirmButton = {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .width(144.dp)
                            .height(36.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            "Cancel",
                            color = Color(0xFF606066),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://3dicomviewer.com/knowledgebase"))
                            onDismiss()
                            context.startActivity(intent)
                        },
                    ) {
                        Text(
                            "OK",
                            color = Color(0xFF50A5DE),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            },
            properties =
                DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                ),
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White,
            modifier =
                Modifier
                    .width(280.dp)
                    .height(252.dp),
        )
    }
}
