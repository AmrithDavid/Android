@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.TitleColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportScreen() {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ReportTopBar()

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            Spacer(modifier = Modifier.height(13.dp))

            PatientInfoCard()

            ReportSection("Indications", "Lorem ipsum dolor sit amet.")
            ReportSection(
                "Procedure",
                "Lorem ipsum dolor sit amet, bifehf34fb34uibf43fyhuuibf3ubfy3fb3fyu3bfy34ubf34yfub34yfub34yfb34fuyb34fyubfyu34frg3grgr3gr3gwgwrgrewgr3gr3g2r3g.",
            )
            ReportSection(
                "Findings",
                "Lorem ipsum dolor sit amet, uhuiwhdquwdhquiwdquiwdhquiwdhquiwhduwdhqwuidhqwidhqwidhqwwidhqwdhuqwhduiqwdhiquwdhquiwhdqiwuhdquiwdhquiwdquiwdhquiwdhqwhuiqwdhquiwd.",
            )
            ReportSection("Impressions", "Lorem ipsum dolor sit amet.")

            Spacer(modifier = Modifier.height(5.dp))

            ActionButtons()


        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PatientInfoCard() {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            PatientInfoRow("Study Description: ", "CT Scan")
            PatientInfoRow("Patient name: ", "Sam Kellahan :(")
            PatientInfoRow("Patient ID: ", "12345")
            PatientInfoRow("Date of Birth: ", "01/01/1980")
            PatientInfoRow("Study Date: ", "06/15/2023")
            PatientInfoRow("Physician's name: ", "David, Amrith")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PatientInfoRow(
    label: String,
    value: String,
) {
    Row {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportSection(
    title: String,
    content: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(color = TitleColor),
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ActionButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Button(
            onClick = { /* TODO: Handle view images click */ },
            modifier =
                Modifier
                    .width(147.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            border = BorderStroke(2.dp, DarkBlue),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_radiology),
                    contentDescription = "Radiology Icon",
                    tint = DarkBlue,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "View Images",
                    style = MaterialTheme.typography.labelLarge,
                    color = DarkBlue,
                )
            }
        }

        Button(
            onClick = { /* TODO: Handle download PDF click */ },
            modifier =
                Modifier
                    .width(161.dp)
                    .height(40.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "Download Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Download PDF",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportTopBar() {
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
                text = "Report",
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
fun ReportScreenPreview() {
    Android3DicomTheme {
        ReportScreen()
    }
}
