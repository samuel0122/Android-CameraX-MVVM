package es.ua.eps.camerax_mvvm.data

import android.graphics.Bitmap
import android.net.Uri
import es.ua.eps.camerax_mvvm.data.dataPreferences.DataPreferencesDao
import es.ua.eps.camerax_mvvm.data.mediaStorage.MediaStorageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaStorageDao: MediaStorageDao, private val dataPreferencesDao: DataPreferencesDao
) {
    fun getLastSavedMediaUri(): Flow<Uri?> = dataPreferencesDao.fetchLastPictureUri()

    fun getMediaThumbnail(uri: Uri) = mediaStorageDao.loadImage(uri)

    suspend fun saveImage(image: Bitmap): Boolean {
        return mediaStorageDao.saveImage(image)?.let { savedImageUri ->
            dataPreferencesDao.saveLastPictureUri(savedImageUri)
            true
        } ?: false
    }
}
