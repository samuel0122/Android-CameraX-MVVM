package es.oliva.samuel.camerax_mvvm.domain.mappers

import es.oliva.samuel.camerax_mvvm.data.dataPreferences.models.LastSavedMediaModel
import es.oliva.samuel.camerax_mvvm.domain.models.LastSavedMediaItem

fun LastSavedMediaModel.toItem(): LastSavedMediaItem =
    LastSavedMediaItem(mediaUri = uri, mediaType = mediaType)

fun LastSavedMediaItem.toModel(): LastSavedMediaModel =
    LastSavedMediaModel(uri = mediaUri, mediaType = mediaType)
