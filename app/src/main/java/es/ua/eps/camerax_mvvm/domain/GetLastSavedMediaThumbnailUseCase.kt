package es.ua.eps.camerax_mvvm.domain

import android.graphics.Bitmap
import es.ua.eps.camerax_mvvm.data.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLastSavedMediaThumbnailUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<Bitmap?> = mediaRepository.getLastSavedMediaUri().map { uri ->
        uri?.let { mediaRepository.getMediaThumbnail(uri) }
    }
}
