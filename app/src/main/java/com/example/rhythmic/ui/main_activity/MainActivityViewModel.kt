package com.example.rhythmic.ui.main_activity

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

class MainActivityViewModel(application: Application) : AndroidViewModel(application){
        private val REQUEST_CODE: Int = 1
        fun getRuntimePermission(activity: Activity) {
                if (isPermissionDenied()
                ) {
                        ActivityCompat.requestPermissions(
                                activity ,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                REQUEST_CODE
                        )
                } else {

                }
        }
        private fun isPermissionDenied() = ContextCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED

        fun onRequestPermissionResult(requestCode: Int, grantResult: Int, activity: Activity) {
                if (requestCode == REQUEST_CODE) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(
                                        getApplication(),
                                        "Permission Granted",
                                        Toast.LENGTH_SHORT
                                ).show()
                        } else {
                                ActivityCompat.requestPermissions(
                                        activity,
                                        arrayOf(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ),
                                        REQUEST_CODE
                                )
                        }
                }
        }
}