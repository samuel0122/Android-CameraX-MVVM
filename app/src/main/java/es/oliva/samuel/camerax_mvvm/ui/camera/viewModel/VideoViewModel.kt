package es.oliva.samuel.camerax_mvvm.ui.camera.viewModel

import android.content.Context
import android.media.AudioManager
import android.os.Vibrator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.ui.common.camera.AbstractVideoViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    audioManager: AudioManager,
    vibrator: Vibrator
) : AbstractVideoViewModel(context, audioManager, vibrator) {

    private val _didSaveVideo = MutableLiveData<Boolean>()

    val didSaveVideo: LiveData<Boolean> get() = _didSaveVideo

    override fun onCreate() {
        super.onCreate()
        _didSaveVideo.postValue(false)
    }

    override fun acceptRecordedVideo() {
        _didSaveVideo.postValue(true)
    }
}
