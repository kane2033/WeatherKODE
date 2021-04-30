package com.kode.weather.presentation.base

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionRequester(fragment: Fragment) {

    //private val action: () -> Unit = {}
    //private val failure: () -> Unit = {}

    private val requestPermission: (onGranted: () -> Unit, onRejected: () -> Unit) -> ActivityResultLauncher<String> =
        { onGranted, onRejected ->
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    onGranted()
                } else {
                    // Разрешение не получено, поясняем, почему фича не работает
                    onRejected()
                    //makeToast(R.string.error_permission_location)
                }
            }
        }
}