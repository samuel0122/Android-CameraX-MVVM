package es.oliva.samuel.camerax_mvvm.domain

import android.graphics.Bitmap
import es.oliva.samuel.camerax_mvvm.data.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLastSavedMediaThumbnailUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<Bitmap?> = mediaRepository.getLastSavedMediaItem()
        .map { mediaItem ->
            mediaItem?.let {
                mediaRepository.getMediaThumbnail(
                    mediaUri = mediaItem.mediaUri,
                    mediaType = mediaItem.mediaType
                )
            }
        }
}
