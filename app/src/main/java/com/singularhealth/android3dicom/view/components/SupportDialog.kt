package com.singularhealth.android3dicom.view.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupportDialog(
    onDismissRequest: () -> Unit,
    context: Context,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier =
                Modifier
                    .size(width = 280.dp, height = 252.dp),
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
                    painter = painterResource(id = R.drawable.ic_support),
                    contentDescription = "Support",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF1D1D1F),
                )
                Text(
                    text = "Visit the customer support website?",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Got a question or need some help? We are here to help.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                    textAlign = TextAlign.Left,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel", color = Color(0xFF606066))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://3dicomviewer.com/knowledgebase"))
                        onDismissRequest()
                        context.startActivity(intent) // Use context to start the activity
                    }) {
                        Text("OK", color = Color(0xFF50A5DE))
                    }
                }
            }
        }
    }
}
