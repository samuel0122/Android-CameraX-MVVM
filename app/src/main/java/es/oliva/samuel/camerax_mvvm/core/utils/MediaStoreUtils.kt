package es.oliva.samuel.camerax_mvvm.core.utils

import android.content.ContentResolver
import android.net.Uri

object MediaStoreUtils {
    fun isImage(uri: Uri, contentResolver: ContentResolver): Boolean {
        val type = contentResolver.getType(uri)
        return type?.startsWith("image/") == true
    }

    fun isVideo(uri: Uri, contentResolver: ContentResolver): Boolean {
        val type = contentResolver.getType(uri)
        return type?.startsWith("video/") == true
    }
}
