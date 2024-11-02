package es.ua.eps.camerax_mvvm.data.dataPreferences

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
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

    override suspend fun saveLastPictureUri(uri: Uri) {
        context.dataStore.edit { preferences ->
            Log.e("DataStorePreferencesDao", "SAVING URI: $uri")
            preferences[KEYS.LAST_PICTURE_URI] = uri.toString()
        }
    }

    override fun fetchLastPictureUri(): Flow<Uri?> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        Log.e("DataStorePreferencesDao", "LOADING")
        preferences[KEYS.LAST_PICTURE_URI]?.toUri()
    }

    companion object KEYS {
        val LAST_PICTURE_URI = stringPreferencesKey("last_picture_uri")
    }
}
