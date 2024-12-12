package es.oliva.samuel.camerax_mvvm.data.mediaStorage

import android.graphics.Bitmap
import android.net.Uri

interface MediaStorageDao {
    fun saveImage(image: Bitmap): Uri?

    fun loadImage(imageUri: Uri): Bitmap?

    fun loadMediaThumbnail(mediaUri: Uri): Bitmap?
}
