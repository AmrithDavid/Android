@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.ui.theme.TitleColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ReportTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = MaterialTheme.shapes.medium,
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row {
                        Text(
                            "Study Description: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "CT Scan",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                    Row {
                        Text(
                            "Patient name: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "Sam Kellahan :(",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                    Row {
                        Text(
                            "Patient ID: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "12345",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                    Row {
                        Text(
                            "Date of Birth: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "01/01/1980",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                    Row {
                        Text(
                            "Study Date: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "06/15/2023",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                    Row {
                        Text(
                            "Physician's name: ",
                            style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor),
                        )
                        Text(
                            "David,Amrith",
                            style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Indications",
                style = MaterialTheme.typography.headlineMedium.copy(color = TitleColor),
            )
            Text(
                text = "Lorem ipsum dolor sit amet.",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Procedure",
                style = MaterialTheme.typography.headlineMedium.copy(color = TitleColor),
            )

            Text(
                text = @Suppress("ktlint:standard:max-line-length")
                "Lorem ipsum dolor sit amet, bifehf34fb34uibf43fyhuuibf3ubfy3fb3fyu3bfy34ubf34yfub34yfub34yfb34fuyb34fyubfyu34frg3grgr3gr3gwgwrgrewgr3gr3g2r3g.",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Findings",
                style = MaterialTheme.typography.headlineMedium.copy(color = TitleColor),
            )

            Text(
                text = @Suppress("ktlint:standard:max-line-length")
                "Lorem ipsum dolor sit amet, uhuiwhdquwdhquiwdquiwdhquiwdhquiwhduwdhqwuidhqwidhqwidhqwwidhqwdhuqwhduiqwdhiquwdhquiwhdqiwuhdquiwdhquiwdquiwdhquiwdhqwhuiqwdhquiwd.",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Impressions",
                style = MaterialTheme.typography.headlineMedium.copy(color = TitleColor),
            )
            Text(
                text = @Suppress("ktlint:standard:max-line-length")
                "Lorem ipsum dolor sit amet.",
                style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { /* TODO: Handle view images click */ },
                    modifier = Modifier
                        .width(147.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(2.dp, DarkBlue),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(start = 8.dp, end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_radiology),
                            contentDescription = "Radiology Icon",
                            tint = DarkBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "View Images",
                            style = MaterialTheme.typography.labelLarge,
                            color = DarkBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = { /* TODO: Handle download PDF click */ },
                    modifier = Modifier
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "Download Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Download PDF",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBlue),
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Row(
            modifier = Modifier
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
