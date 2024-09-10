package com.example.dma_swa_001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import com.example.dma_swa_001.ui.theme.Dmaswa001Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dmaswa001Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopNavigationBar() },
                    containerColor = Color(0xFFFFFFFF)
                ) { innerPadding ->
                    CardList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TopNavigationBar() {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = emptyList()
    )

    val plusJakartaSans = GoogleFont("Plus Jakarta Sans")
    val plusJakartaSansFamily = FontFamily(
        Font(googleFont = plusJakartaSans, fontProvider = provider)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2E3176))
            .padding(top = statusBarHeight)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(27.dp))

            Text(
                text = "Hello Sam",
                style = TextStyle(
                    fontFamily = plusJakartaSansFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                value = "",
                onValueChange = { /* TODO: Handle search input */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E1FF).copy(alpha = 0.3f)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        "Search",
                        color = Color.White.copy(alpha = 0.5f),
                        style = TextStyle(
                            fontFamily = plusJakartaSansFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_voice),
                        contentDescription = "Voice Search",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = TextStyle(
                    fontFamily = plusJakartaSansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.sp,
                    color = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E1FF).copy(alpha = 0.3f))
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun CardList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(4) {
            EmptyCard(patientName = "Sam Kellahan")
        }
    }
}

@Composable
fun EmptyCard(
    patientName: String = "John Doe",
    date: String = "2024-09-10",
    patientId: String = "123456789",
    modality: String = "CT",
    expiresIn: String = "7 days",
    imageRes: Int = R.drawable.patient_image
) {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = emptyList()
    )

    val plusJakartaSans = GoogleFont("Plus Jakarta Sans")
    val plusJakartaSansFamily = FontFamily(
        Font(googleFont = plusJakartaSans, fontProvider = provider)
    )

    val buttonColor = Color(0xFF2E3176)
    val titleColor = Color(0xFF1D1D1F)
    val subheadingColor = Color(0xFF606066)
    val dividerColor = Color(0xFFD1D1DB)

    val labelStyle = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        color = subheadingColor
    )

    val contentStyle = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        color = subheadingColor
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Image
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Patient Image",
                    modifier = Modifier
                        .width(80.dp)
                        .height(125.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit
                )

                // Content
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)) {
                    Text(
                        text = "Study Description",
                        style = TextStyle(
                            fontFamily = plusJakartaSansFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            letterSpacing = 0.sp,
                            color = titleColor
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patientName,
                        style = TextStyle(
                            fontFamily = plusJakartaSansFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.sp,
                            color = subheadingColor
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = dividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text("Date: ", style = labelStyle)
                        Text(date, style = contentStyle)
                    }
                    Row {
                        Text("Patient ID: ", style = labelStyle)
                        Text(patientId, style = contentStyle)
                    }
                    Row {
                        Text("Modality: ", style = labelStyle)
                        Text(modality, style = contentStyle)
                    }
                    Row {
                        Text("Expires in: ", style = labelStyle)
                        Text(expiresIn, style = contentStyle)
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "More options",
                    tint = buttonColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonData = listOf(
                    Triple("Images", R.drawable.ic_radiology, "Radiology Icon"),
                    Triple("Report", R.drawable.ic_report, "Report Icon"),
                    Triple("Share", R.drawable.ic_share, "Share Icon")
                )
                buttonData.forEach { (text, iconRes, contentDescription) ->
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFFFFFFF)
                        ),
                        border = BorderStroke(2.dp, buttonColor),
                        shape = RoundedCornerShape(9.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = contentDescription,
                                tint = buttonColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = text,
                                style = TextStyle(
                                    fontFamily = plusJakartaSansFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    lineHeight = 14.sp,
                                    letterSpacing = 0.12.sp,
                                    color = buttonColor
                                ),
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


// Preview function to visualise the CardList
@Preview(showBackground = true)
@Composable
fun CardListPreview() {
    Dmaswa001Theme {
        Scaffold(
            topBar = { TopNavigationBar() },
            containerColor = Color(0xFFFFFFFF)
        ) { innerPadding ->
            CardList(modifier = Modifier.padding(innerPadding))
        }
    }
}


