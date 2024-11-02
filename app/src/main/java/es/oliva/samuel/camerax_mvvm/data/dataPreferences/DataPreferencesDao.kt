package es.oliva.samuel.camerax_mvvm.data.dataPreferences

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface DataPreferencesDao {
    suspend fun saveLastPictureUri(uri: Uri)

    fun fetchLastPictureUri(): Flow<Uri?>
}
