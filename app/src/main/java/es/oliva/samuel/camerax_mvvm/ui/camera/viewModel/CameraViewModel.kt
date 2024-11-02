package es.oliva.samuel.camerax_mvvm.ui.camera.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.oliva.samuel.camerax_mvvm.domain.SaveImageUseCase
import es.oliva.samuel.camerax_mvvm.ui.common.camera.AbstractCameraViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveImageUseCase: SaveImageUseCase
) : AbstractCameraViewModel() {

    private val _didInsertPage = MutableLiveData<Boolean>()

    val didInsertPage: LiveData<Boolean> get() = _didInsertPage

    override fun onCreate() {
        super.onCreate()
        _didInsertPage.postValue(false)
    }

    fun insertCapturedPage() {
        viewModelScope.launch {
            pictureBitmap.value?.let { pictureBitmap ->
                if (saveImageUseCase(pictureBitmap)) _didInsertPage.postValue(true)
            }
        }
    }
}
