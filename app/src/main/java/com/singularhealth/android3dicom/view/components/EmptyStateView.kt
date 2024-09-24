@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.TitleColor
import com.singularhealth.android3dicom.ui.theme.WebPortalBlue

@Suppress("ktlint:standard:function-naming")
@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_person__1_),
            contentDescription = "No scans available",
            contentScale = ContentScale.Fit,
            modifier =
            Modifier
                .width(350.dp)
                .height(800.dp)
                .offset(y = 400.dp),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .padding(bottom = 50.dp)
                .offset(y = (-350).dp),
        ) {
            Text(
                "There are currently no scans to view",
                style = MaterialTheme.typography.titleLarge,
                color = TitleColor,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 8.dp),
            )
            Text(
                "Scans you upload via the web portal will be viewable here.",
                style = MaterialTheme.typography.bodyLarge,
                color = SubheadingColor,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 16.dp),
            )

            Button(
                onClick = { /* TODO: Handle button click */ },
                colors = ButtonDefaults.buttonColors(containerColor = WebPortalBlue),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_browser),
                    contentDescription = "Web Portal Icon",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Web Portal",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun EmptyStateViewPreview() {
    Android3DicomTheme {
        EmptyStateView()
    }
}
