package es.oliva.samuel.camerax_mvvm.data.dataPreferences.models

import android.net.Uri
import es.oliva.samuel.camerax_mvvm.core.eMediaType

data class LastSavedMediaModel(
    var uri: Uri,
    var mediaType: eMediaType
)
