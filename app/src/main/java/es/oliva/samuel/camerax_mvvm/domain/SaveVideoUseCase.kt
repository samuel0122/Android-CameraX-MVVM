package es.oliva.samuel.camerax_mvvm.domain

import android.net.Uri
import es.oliva.samuel.camerax_mvvm.data.MediaRepository
import javax.inject.Inject

class SaveVideoUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(videoUri: Uri): Boolean = mediaRepository.saveVideo(videoUri)
}
