package es.oliva.samuel.camerax_mvvm.domain

import android.graphics.Bitmap
import es.oliva.samuel.camerax_mvvm.data.MediaRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(image: Bitmap): Boolean = mediaRepository.saveImage(image)
}
