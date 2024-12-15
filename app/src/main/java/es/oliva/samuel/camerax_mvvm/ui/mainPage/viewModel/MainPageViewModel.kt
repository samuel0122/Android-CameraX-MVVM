package es.oliva.samuel.camerax_mvvm.ui.mainPage.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.oliva.samuel.camerax_mvvm.domain.GetLastSavedMediaThumbnailUseCase
import es.oliva.samuel.camerax_mvvm.domain.GetLastSavedMediaUseCase
import es.oliva.samuel.camerax_mvvm.domain.models.LastSavedMediaItem
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getLastSavedMediaThumbnailUseCase: GetLastSavedMediaThumbnailUseCase,
    private val getLastSavedMediaUseCase: GetLastSavedMediaUseCase
) : ViewModel() {
    private val _lastSavedMediaThumbnail = MutableLiveData<Bitmap?>()
    private val _lastSavedMedia = MutableLiveData<LastSavedMediaItem?>()

    val lastSavedMediaThumbnail: LiveData<Bitmap?> = _lastSavedMediaThumbnail
    val lastSavedMedia: LiveData<LastSavedMediaItem?> = _lastSavedMedia

    fun onCreate() {
        viewModelScope.launch {
            getLastSavedMediaThumbnailUseCase().collect { bitmap ->
                _lastSavedMediaThumbnail.postValue(bitmap)
            }
        }

        viewModelScope.launch {
            getLastSavedMediaUseCase().collect { lastSavedMedia ->
                _lastSavedMedia.postValue(lastSavedMedia)
            }
        }
    }
}
