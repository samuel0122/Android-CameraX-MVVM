package es.oliva.samuel.camerax_mvvm.data.dataPreferences

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.core.eMediaType
import es.oliva.samuel.camerax_mvvm.data.dataPreferences.models.LastSavedMediaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class DataStorePreferencesDao @Inject constructor(
    @ApplicationContext private val context: Context
) : DataPreferencesDao {

    override suspend fun saveLastMedia(uri: Uri, mediaType: eMediaType): Boolean =
        try {
            context.dataStore.edit { preferences ->
                preferences[KEYS.LAST_PICTURE_URI] = uri.toString()
                preferences[KEYS.LAST_MEDIA_TYPE] = mediaType.value
            }
            true
        } catch (e: Exception) {
            false
        }

    override fun fetchLastSavedMedia(): Flow<LastSavedMediaModel?> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val uri = preferences[KEYS.LAST_PICTURE_URI]?.toUri()
                val mediaType = preferences[KEYS.LAST_MEDIA_TYPE]?.let { eMediaType.fromInt(it) }
                Log.d("DataStorePreferencesDao", "lastSavedMediaFlow")
                if (uri != null && mediaType != null)
                    LastSavedMediaModel(uri, mediaType)
                else
                    null
            }

    companion object KEYS {
        val LAST_PICTURE_URI = stringPreferencesKey("last_picture_uri")
        val LAST_MEDIA_TYPE = intPreferencesKey("last_media_uri")
    }
}
