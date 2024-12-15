package es.oliva.samuel.camerax_mvvm.domain.models

import android.net.Uri
import es.oliva.samuel.camerax_mvvm.core.eMediaType

data class LastSavedMediaItem(
    val mediaUri: Uri,
    val mediaType: eMediaType
)
