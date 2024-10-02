package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel

class ImageDetailViewModel : ViewModel(){

    fun on3DClick() {
        // Display 3D scan image
        println("clicked 3D")
    }

    fun onTransverseClick() {
        // Display Transverse scan image
        println("clicked Transverse")
    }

    fun onSagittalClick() {
        // Display Sagittal scan image
        println("clicked Sagittal")
    }

    fun onCoronalClick() {
        // Display Coronal scan image
        println("clicked Coronal")
    }

}