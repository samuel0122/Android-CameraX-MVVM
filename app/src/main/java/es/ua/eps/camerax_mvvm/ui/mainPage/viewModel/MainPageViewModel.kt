package es.ua.eps.camerax_mvvm.ui.mainPage.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ua.eps.camerax_mvvm.domain.GetLastSavedMediaThumbnailUseCase
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getLastSavedMediaThumbnailUseCase: GetLastSavedMediaThumbnailUseCase,
) : ViewModel() {
    private val _lastSavedMediaThumbnail = MutableLiveData<Bitmap?>()

    val lastSavedMediaThumbnail: LiveData<Bitmap?> = _lastSavedMediaThumbnail

    fun onCreate() {
        Log.e("DataStorePreferencesDao", "onCreate")
        viewModelScope.launch {
            getLastSavedMediaThumbnailUseCase().onCompletion { // CALLED WHEN THE SCREEN IS ROTATED OR HOME BUTTON PRESSED
                Log.d("DataStorePreferencesDao", "viewModelScope onCompletion")
            }.collect { bitmap ->
                _lastSavedMediaThumbnail.postValue(bitmap)
            }
        }
    }
}
