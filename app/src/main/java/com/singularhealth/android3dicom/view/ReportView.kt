@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.TitleColor
import com.singularhealth.android3dicom.viewmodel.ReportViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportView(viewModel: ReportViewModel = hiltViewModel()) {
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
fun PatientInfoCard(viewModel: ReportViewModel = hiltViewModel()) {
    val cardData by viewModel.cardData.collectAsState()

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
            PatientInfoRow("Study Description: ", cardData.fileName)
            PatientInfoRow("Patient name: ", cardData.patientName)
            PatientInfoRow("Patient ID: ", cardData.patientId)
            PatientInfoRow("Date of Birth: ", cardData.date)
            PatientInfoRow("Study Date: ", cardData.date)
            PatientInfoRow("Physician's name: ", cardData.patientName)
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
fun ActionButtons(viewModel: ReportViewModel = hiltViewModel()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Button(
            onClick = { viewModel.onViewImages() },
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
            onClick = { viewModel.onDownloadPdf() },
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
fun ReportTopBar(viewModel: ReportViewModel = hiltViewModel()) {
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
                modifier = Modifier.size(24.dp).clickable { viewModel.onBack() },
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
        ReportView()
    }
}
