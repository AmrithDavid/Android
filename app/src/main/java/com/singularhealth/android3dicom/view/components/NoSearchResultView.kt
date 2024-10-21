@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
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
fun NoSearchResultView() {

    val context = LocalContext.current

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
                .offset(y = (-470).dp),
        ) {
            Text(
                "Sorry, we couldn't find what you're looking for",
                style = MaterialTheme.typography.titleLarge,
                color = TitleColor,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 8.dp),
            )
            Text(
                "Please try a different search term.",
                style = MaterialTheme.typography.bodyLarge,
                color = SubheadingColor,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 16.dp),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun NoSearchResultViewPreview() {
    Android3DicomTheme {
        NoSearchResultView()
    }
}
