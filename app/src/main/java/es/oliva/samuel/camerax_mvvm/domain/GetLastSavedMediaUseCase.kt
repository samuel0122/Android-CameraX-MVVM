package es.oliva.samuel.camerax_mvvm.domain

import es.oliva.samuel.camerax_mvvm.data.MediaRepository
import es.oliva.samuel.camerax_mvvm.domain.models.LastSavedMediaItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastSavedMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<LastSavedMediaItem?> =
        mediaRepository.getLastSavedMediaItem()
}
