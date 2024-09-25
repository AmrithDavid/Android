package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.ui.theme.DividerColor
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Suppress("ktlint:standard:function-naming")
// ScanCard is a composable function that represents a single card in the list
@Composable
fun ScanCard(
    patientCardData: PatientCardData,
    onImageButtonClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true,
                ),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Image(
                    painter =
                        painterResource(
                            id =
                                LocalContext.current.resources.getIdentifier(
                                    patientCardData.imageName,
                                    "drawable",
                                    LocalContext.current.packageName,
                                ),
                        ),
                    contentDescription = "Patient Image",
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(125.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit,
                )

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                ) {
                    Text(
                        text = "Study Description",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patientCardData.patientName,
                        style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text("Date: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCardData.date, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Patient ID: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCardData.patientId, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Modality: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCardData.modality, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Expires in: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCardData.expiresIn, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val buttonData =
                    listOf(
                        Triple("Images", R.drawable.ic_radiology, "Radiology Icon"),
                        Triple("Report", R.drawable.ic_report, "Report Icon"),
                        Triple("Share", R.drawable.ic_share, "Share Icon"),
                    )
                buttonData.forEachIndexed { index, (text, iconRes, contentDescription) ->
                    OutlinedButton(
                        onClick = { if (index == 0) onImageButtonClick() },
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(40.dp),
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(9.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = contentDescription,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                                maxLines = 1,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
