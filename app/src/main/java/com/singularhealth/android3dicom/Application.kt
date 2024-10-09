package com.singularhealth.android3dicom

import android.app.Application
import com.singularhealth.android3dicom.utilities.KeystorePinHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KeystorePinHandler.initialise(this)
    }
}
