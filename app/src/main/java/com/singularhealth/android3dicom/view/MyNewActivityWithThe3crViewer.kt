package com.singularhealth.android3dicom.view

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.compose.rememberNavController
import health.singular.viewer3cr.android.sdk.FrontEndInterfaces
import health.singular.viewer3cr.android.sdk.FrontEndPayload
import health.singular.viewer3cr.android.sdk.ViewerSdkActivity

// NOTE: This file is for testing, not production.

@Suppress("ktlint:standard:property-naming")
const val sampleScanPayload =
    "{" +
        "\"Url\":\"https://webgl-3dr.singular.health/test_scans/01440d4e-8b04-4b90-bb2c-698535ce16d6/CHEST.3vxl\"," +
        "\"DecryptionKey\":{" +
        "\"Iv\":\"XEloSh+OcO7TG77au6HjPw==\"," +
        "\"Key\":\"KUc722X1y4w42M+jCf9a3+6EGz66z7UMWK3m2aMqGxM=\"" +
        "}" +
        "}"

class MyNewActivityWithThe3crViewer : ViewerSdkActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        printOpenGLVersion()

        val showMainButton =
            Button(this).apply {
                text = "LOAD SCAN"
                x = 10f
                y = 250f
                setOnClickListener {
                    loadSampleScan()
                }
            }

        val linearContainer = LinearLayout(this)
        linearContainer.addView(
            ComposeView(this).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MainImageMenuWrapper()
                }
            },
        )

        addView(linearContainer)
        addView(showMainButton)
    }

    override fun onCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet,
    ): View? = super.onCreateView(name, context, attrs)

    private fun loadSampleScan() {
        println("Loading the sample scan...")
        // Call this function whenever you would like 3CR to perform an action
        // You will need to supply different arguments based on the documentation
        executePayload(
            FrontEndPayload(
                FrontEndInterfaces.FILE_MANAGEMENT,
                "fm_01",
                sampleScanPayload,
                "1.0.0",
            ),
        )
    }

    // This function will be called every time 3CR emits a message to the Front End.
    // override it so you can process the results from 3CR.
    override fun onPayload(jsonPayload: FrontEndPayload?) {
        super.onPayload(jsonPayload)

        println("Printing the JSON payload returned from SDK")
        println(jsonPayload)
        // ... Do something with payload.
    }

    private fun printOpenGLVersion() {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        println("Device Supported OpenGL ES Version = " + configurationInfo.getGlEsVersion())
    }

    @Suppress("ktlint:standard:function-naming")
    @Composable
    private fun MainImageMenuWrapper() {
        val navController = rememberNavController()
        MainImageMenu(navController = navController)
    }
}
