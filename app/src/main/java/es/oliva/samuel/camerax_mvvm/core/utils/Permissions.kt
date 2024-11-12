package es.oliva.samuel.camerax_mvvm.core.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import es.oliva.samuel.camerax_mvvm.R

object Permissions {
    const val REQUEST_CODE_CAMERA = 100
    const val REQUEST_CODE_VIDEO = 101

    fun requestCameraPermissions(activity: Activity) {
        EasyPermissions.requestPermissions(
            host = activity,
            rationale = activity.applicationContext.getString(R.string.cameraPermissionText),
            requestCode = REQUEST_CODE_CAMERA,
            perms = arrayOf(android.Manifest.permission.CAMERA)
        )
    }

    fun requestCameraPermissions(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            host = fragment,
            rationale = fragment.context?.getString(R.string.cameraPermissionText).orEmpty(),
            requestCode = REQUEST_CODE_CAMERA,
            perms = arrayOf(android.Manifest.permission.CAMERA)
        )
    }

    fun requestVideoPermissions(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            host = fragment,
            rationale = fragment.context?.getString(R.string.cameraPermissionText).orEmpty(),
            requestCode = REQUEST_CODE_VIDEO,
            perms = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            )
        )
    }
}
