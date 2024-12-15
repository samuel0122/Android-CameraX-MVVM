package es.oliva.samuel.camerax_mvvm.data

import android.graphics.Bitmap
import android.net.Uri
import es.oliva.samuel.camerax_mvvm.core.eMediaType
import es.oliva.samuel.camerax_mvvm.data.dataPreferences.DataPreferencesDao
import es.oliva.samuel.camerax_mvvm.data.mediaStorage.MediaStorageDao
import es.oliva.samuel.camerax_mvvm.domain.mappers.toItem
import es.oliva.samuel.camerax_mvvm.domain.models.LastSavedMediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaStorageDao: MediaStorageDao, private val dataPreferencesDao: DataPreferencesDao
) {
    fun getLastSavedMediaItem(): Flow<LastSavedMediaItem?> =
        dataPreferencesDao.fetchLastSavedMedia()
            .map { it?.toItem() }

    fun getMediaThumbnail(mediaUri: Uri, mediaType: eMediaType) =
        mediaStorageDao.loadMediaThumbnail(mediaUri, mediaType)

    suspend fun saveImage(image: Bitmap): Boolean =
        mediaStorageDao.savePicture(image)
            ?.let { savedImageUri ->
                dataPreferencesDao.saveLastMedia(savedImageUri, eMediaType.Picture)
                true
            } ?: false

    suspend fun saveVideo(videoUri: Uri): Boolean =
        dataPreferencesDao.saveLastMedia(videoUri, eMediaType.Video)
}
