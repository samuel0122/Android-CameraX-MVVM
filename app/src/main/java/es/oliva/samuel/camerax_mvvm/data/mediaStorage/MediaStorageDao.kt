package es.oliva.samuel.camerax_mvvm.data.mediaStorage

import android.graphics.Bitmap
import android.net.Uri
import es.oliva.samuel.camerax_mvvm.core.eMediaType

interface MediaStorageDao {
    fun savePicture(picture: Bitmap): Uri?

    fun loadPicture(pictureUri: Uri): Bitmap?

    fun loadMediaThumbnail(mediaUri: Uri, mediaType: eMediaType): Bitmap?
}
