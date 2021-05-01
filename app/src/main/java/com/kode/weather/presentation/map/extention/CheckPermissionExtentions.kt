package com.kode.weather.presentation.map.extention

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Fragment.permissionActivityResultContract(onGranted: () -> Unit, onRejected: () -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Разрешение получено
            onGranted()
        } else {
            // Разрешение не получено
            onRejected()
        }
    }

