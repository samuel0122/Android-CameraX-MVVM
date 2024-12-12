package es.oliva.samuel.camerax_mvvm.ui.preview.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.oliva.samuel.camerax_mvvm.domain.SaveImageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoPreviewViewModel @Inject constructor(
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {
    private val _photoBitmap = MutableLiveData<Bitmap>()
    private val _didInsertPhoto = MutableLiveData<Boolean>()
    private val _didRejectPhoto = MutableLiveData<Boolean>()

    val photoBitmap: LiveData<Bitmap> get() = _photoBitmap
    val didAcceptPhoto: LiveData<Boolean> get() = _didInsertPhoto
    val didRejectPhoto: LiveData<Boolean> get() = _didRejectPhoto

    fun onCreate(photo: Bitmap) {
        _photoBitmap.postValue(photo)
        _didInsertPhoto.postValue(false)
        _didRejectPhoto.postValue(false)
    }

    fun acceptCapturedPicture() {
        viewModelScope.launch {
            photoBitmap.value?.let { photoBitmap ->
                if (saveImageUseCase(photoBitmap)) _didInsertPhoto.postValue(true)
            }
        }
    }

    fun rejectCapturedPicture() {
        _didRejectPhoto.postValue(true)
    }
}
