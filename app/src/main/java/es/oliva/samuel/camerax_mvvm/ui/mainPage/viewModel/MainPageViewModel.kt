package es.oliva.samuel.camerax_mvvm.ui.mainPage.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.oliva.samuel.camerax_mvvm.domain.GetLastSavedMediaThumbnailUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getLastSavedMediaThumbnailUseCase: GetLastSavedMediaThumbnailUseCase,
) : ViewModel() {
    private val _lastSavedMediaThumbnail = MutableLiveData<Bitmap?>()

    val lastSavedMediaThumbnail: LiveData<Bitmap?> = _lastSavedMediaThumbnail

    fun onCreate() {
        viewModelScope.launch {
            getLastSavedMediaThumbnailUseCase().collect { bitmap ->
                _lastSavedMediaThumbnail.postValue(bitmap)
            }
        }
    }
}
