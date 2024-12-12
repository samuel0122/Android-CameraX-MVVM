package es.oliva.samuel.camerax_mvvm.data.mediaStorage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.core.utils.MediaStoreUtils
import es.oliva.samuel.camerax_mvvm.core.utils.SaveToMediaStore
import javax.inject.Inject

class ImagesStorageDao @Inject constructor(
    @ApplicationContext private var context: Context
) : MediaStorageDao {
    override fun saveImage(image: Bitmap): Uri? {
        val saveFileName = SaveToMediaStore.generateImageFileName()
        val saveFileUri = SaveToMediaStore.generatePublicPictureUriForImage(context, saveFileName)

        return saveFileUri?.let { uri ->
            if (SaveToMediaStore.saveImageFromBitmap(context, image, uri)) saveFileUri else null
        }
    }

    override fun loadImage(imageUri: Uri): Bitmap? {
        return SaveToMediaStore.loadImageAsBitmap(context, imageUri)
    }

    override fun loadMediaThumbnail(mediaUri: Uri): Bitmap? {
        if (MediaStoreUtils.isImage(mediaUri, context.contentResolver)) {
            return SaveToMediaStore.loadImageAsBitmap(context, mediaUri)
        } else if (MediaStoreUtils.isVideo(mediaUri, context.contentResolver)) {
            return SaveToMediaStore.loadVideoThumbnailAsBitmap(context, mediaUri)
        }

        return null
    }
}
