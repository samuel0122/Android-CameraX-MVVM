package es.oliva.samuel.camerax_mvvm.ui.preview.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.core.utils.SaveToMediaStore
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _recordedVideo = MutableLiveData<Uri>()
    private val _didInsertVideo = MutableLiveData<Boolean>()
    private val _didRejectVideo = MutableLiveData<Boolean>()

    val recordedVideo: LiveData<Uri> get() = _recordedVideo
    val didAcceptVideo: LiveData<Boolean> get() = _didInsertVideo
    val didRejectVideo: LiveData<Boolean> get() = _didRejectVideo

    fun onCreate(video: Uri) {
        _recordedVideo.postValue(video)
        _didInsertVideo.postValue(false)
        _didRejectVideo.postValue(false)
    }

    fun acceptRecordedVideo() {
        _didInsertVideo.postValue(true)
    }

    fun rejectRecordedVideo() {
        viewModelScope.launch {
            recordedVideo.value?.let { videoUri -> SaveToMediaStore.deleteImage(context, videoUri) }
            _didRejectVideo.postValue(true)
        }
    }
}
