@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.SubheadingColor

@Suppress("ktlint:standard:function-naming")
@Composable
fun MoreOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier =
            modifier
                .width(159.dp)
                .background(
                    color = Color(0xFFF7F7F7),
                    shape = MaterialTheme.shapes.small,
                ),
        properties = PopupProperties(focusable = true),
    ) {
        val menuItems =
            listOf(
                "More Info" to R.drawable.ic_info,
                "Delete" to R.drawable.ic_delete,
                "Share" to R.drawable.ic_share,
                "Report" to R.drawable.ic_report,
                "Support" to R.drawable.ic_support,
            )

        menuItems.forEach { (text, iconRes) ->
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = text,
                            tint = SubheadingColor,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SubheadingColor,
                        )
                    }
                },
                onClick = { onItemClick(text) },
            )
        }
    }
}
