package es.oliva.samuel.camerax_mvvm.data.dataPreferences

import android.net.Uri
import es.oliva.samuel.camerax_mvvm.core.eMediaType
import es.oliva.samuel.camerax_mvvm.data.dataPreferences.models.LastSavedMediaModel
import kotlinx.coroutines.flow.Flow

interface DataPreferencesDao {
    suspend fun saveLastMedia(uri: Uri, mediaType: eMediaType): Boolean

    fun fetchLastSavedMedia(): Flow<LastSavedMediaModel?>
}
