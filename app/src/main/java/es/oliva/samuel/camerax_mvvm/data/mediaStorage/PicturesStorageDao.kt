package es.oliva.samuel.camerax_mvvm.data.mediaStorage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.core.eMediaType
import es.oliva.samuel.camerax_mvvm.core.utils.SaveToMediaStore
import javax.inject.Inject

class PicturesStorageDao @Inject constructor(
    @ApplicationContext private var context: Context
) : MediaStorageDao {
    override fun savePicture(picture: Bitmap): Uri? {
        val saveFileName = SaveToMediaStore.generateImageFileName()
        val saveFileUri = SaveToMediaStore.generatePublicPictureUriForImage(context, saveFileName)

        return saveFileUri?.let { uri ->
            if (SaveToMediaStore.saveImageFromBitmap(context, picture, uri)) saveFileUri else null
        }
    }

    override fun loadPicture(pictureUri: Uri): Bitmap? {
        return SaveToMediaStore.loadImageAsBitmap(context, pictureUri)
    }

    override fun loadMediaThumbnail(mediaUri: Uri, mediaType: eMediaType): Bitmap? {
        return when (mediaType) {
            eMediaType.Video -> SaveToMediaStore.loadVideoThumbnailAsBitmap(context, mediaUri)
            eMediaType.Picture -> SaveToMediaStore.loadImageAsBitmap(context, mediaUri)
            else -> null
        }
    }
}
