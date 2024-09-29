package com.singularhealth.android3dicom.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(
                this,
                MyNewActivityWithThe3crViewer::class.java,
            ),
        )
    }
}
