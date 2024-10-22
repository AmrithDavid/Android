package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.ui.theme.DividerColor
import com.singularhealth.android3dicom.ui.theme.LightBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.viewmodel.ScanCardViewModel

@Suppress("ktlint:standard:function-naming")
// ScanCard is a composable function that represents a single card in the list
@Composable
fun ScanCard(
    viewModel: ScanCardViewModel = hiltViewModel(),
    patientCardData: PatientCardData,
) {
    // State to control the visibility of the dropdown menu
    var isMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.data = patientCardData
    }

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
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(125.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                ) {
                    Text(
                        text = "Image",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                ) {
                    Text(
                        // text = patientCardData.fileName,
                        text = "Study Description", // changes this so each card was titled study description
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

//                Icon(
//                    painter = painterResource(id = R.drawable.ic_more),
//                    contentDescription = "More options",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(24.dp),
//                )
//            }
                // Clickable 'More' Icon with Popup Menu
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isMenuExpanded = true } // Open the menu on click
                    )

                    // DropdownMenu to display the popup options
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier
                            .width(159.dp)
                            .height(96.dp)
                            .background(color = Color(0xFFF7F7F7))
                    ) {
                        // First Row - More info Option
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_info),
                                        contentDescription = "More info",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("More info", style = MaterialTheme.typography.bodyMedium)
                                }
                            },
                            onClick = {
                                isMenuExpanded = false
                                viewModel.onMoreInfo() // Handle Edit action
                            },
                            modifier = Modifier.offset(y=-10.dp)
                        )

                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Delete", style = MaterialTheme.typography.bodyMedium)
                                }
                            },
                            onClick = {
                                isMenuExpanded = false
                                viewModel.onDelete() // Handle Edit action
                            },
                            modifier = Modifier.offset(y=-10.dp)
                        )
                    }
                }

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
                        onClick = {
                            when (index) {
                                0 -> viewModel.onImages()
                                1 -> viewModel.onReport()
                                2 -> viewModel.onShare()
                            }
                        },
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
    // Display the dialog if `showMoreInfoDialog` is true
    if (viewModel.showMoreInfoDialog.value) {
        MoreInfoDialog(onDismiss = { viewModel.onCloseMoreInfoDialog() })
    }

    // Display the dialog if `showDeleteDialog` is true
    if (viewModel.showDeleteDialog.value) {
        DeleteDialog(onDismiss = { viewModel.onCloseDeleteDialog() },
            onDeleteConfirm = {
                // Add your delete logic here
                viewModel.performDeleteAction()
            })
    }
}

@Composable
fun MoreInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(width = 280.dp, height = 273.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp),
                ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "Information",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Study description",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    InfoRow("Date added:", "dd/mm/yyyy")
                    InfoRow("Image count:", "257")
                    InfoRow("Series description:", "Chest")
                    InfoRow("Series number:", "3")
                    InfoRow("Instance ID:", "1.2.3.4.5.6.789")
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF50A5DE),
                        ),
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteDialog(onDismiss: () -> Unit, onDeleteConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(width = 280.dp, height = 273.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp),
                ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete Scan",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Delete Scan?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Deleting this scan will mean it is no longer accessible on this device or through the web portal.")
                }

                Spacer(modifier = Modifier.weight(1f))

                // Row to hold the Cancel and Delete buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right,
                ) {
                    // Cancel Button
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF606066)),
                    ) {
                        Text("Cancel")
                    }

                    // Delete Button
                    TextButton(
                        onClick = {
                            onDeleteConfirm() // Call the delete action
                            onDismiss() // Close the dialog
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF50A5DE)),
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = SubheadingColor,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = SubheadingColor,
        )
    }
}

