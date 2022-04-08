package com.example.overplaytask.exts

import android.app.Activity
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Fragment.withAllPermissions(
    vararg permissions: String,
    responseHandler: PermissionResponseHandler? = null
) {
    val activity = activity ?: return
    Dexter.withContext(activity)
        .withPermissions(permissions.toList())
        .withListener(
            object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.areAllPermissionsGranted()?.also {
                        if (it) {
                            responseHandler?.onPermissionGranted()
                        } else {
                            responseHandler?.onPermissionRejected()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        )
        .check()
}

fun Activity.withAllPermissions(vararg permissions: String, responseHandler: PermissionResponseHandler? = null) {
    Dexter.withContext(this)
        .withPermissions(permissions.toList())
        .withListener(
            object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.areAllPermissionsGranted()?.also {
                        if (it) {
                            responseHandler?.onPermissionGranted()
                        } else {
                            responseHandler?.onPermissionRejected()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        )
        .check()
}

interface PermissionResponseHandler {
    fun onPermissionGranted()
    fun onPermissionRejected()
}
