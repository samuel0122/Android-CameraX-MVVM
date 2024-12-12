package es.oliva.samuel.camerax_mvvm.ui.camera.viewModel

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.media.MediaActionSound
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.oliva.samuel.camerax_mvvm.core.utils.SaveToMediaStore
import es.oliva.samuel.camerax_mvvm.ui.camera.CameraFacing
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioManager: AudioManager,
    private val vibrator: Vibrator
) : ViewModel() {
    private val _flashOn = MutableLiveData<Boolean>()
    private val _isCapturingVideo = MutableLiveData<Boolean>()
    private val _cameraFacing = MutableLiveData<CameraFacing>()
    private val _recordedVideo = MutableLiveData<Uri?>()

    val flashOn: LiveData<Boolean> get() = _flashOn
    val isCapturingVideo: LiveData<Boolean> get() = _isCapturingVideo
    val cameraFacing: LiveData<CameraFacing> get() = _cameraFacing
    val recordedVideo: LiveData<Uri?> get() = _recordedVideo

    private var _cameraProvider: ProcessCameraProvider? = null
    private var _camera: Camera? = null

    val camera: Camera? get() = _camera
    val cameraProvider: ProcessCameraProvider? get() = _cameraProvider

    var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    var preview: Preview? = null
    val cameraSelector: CameraSelector
        get() = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var aspectRatio = AspectRatio.RATIO_4_3

    private val cameraExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private val shutterSound: MediaActionSound by lazy { MediaActionSound() }

    private val canPlaySound: Boolean
        get() = audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL
    private val canVibrate: Boolean
        get() = audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL ||
                audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE

    open fun onCreate() {
        shutterSound.load(MediaActionSound.START_VIDEO_RECORDING)
        shutterSound.load(MediaActionSound.STOP_VIDEO_RECORDING)
    }

    fun onDestroy() {
        cameraExecutor.shutdown()
        shutterSound.release()
        cameraProvider?.unbindAll()
    }

    fun onStartCamera(rotation: Int) {
        viewModelScope.launch {
            buildVideoCapture()

            buildPreview(rotation)

            _flashOn.postValue(flashOn.value ?: false)
            _isCapturingVideo.postValue(isCapturingVideo.value ?: false)
            _cameraFacing.postValue(cameraFacing.value ?: CameraFacing.Back)
        }
    }

    fun onStopCamera() {
        videoCapture = null
        preview = null
        _camera = null
        _recordedVideo.postValue(null)
    }

    private fun buildPreview(rotation: Int) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    aspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()

        preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
    }

    private fun buildVideoCapture() {
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )

        val recorder = Recorder.Builder()
            .setExecutor(cameraExecutor)
            .setQualitySelector(qualitySelector)
            .setAspectRatio(aspectRatio)
            .build()

        videoCapture = VideoCapture.withOutput(recorder)
    }

    fun bindCameraUseCases(viewLifecycleOwner: LifecycleOwner) {
        cameraProvider?.unbindAll()

        // Bind use cases to camera
        val camera = cameraProvider?.bindToLifecycle(
            viewLifecycleOwner, cameraSelector, preview, videoCapture
        )

        _camera = camera
    }

    fun setCameraProvider(cameraProvider: ProcessCameraProvider) {
        _cameraProvider = cameraProvider
    }

    fun setCamera(camera: Camera) {
        _camera = camera
    }

    fun flipCamera() {
        cameraFacing.value.let { cameraFacing ->
            viewModelScope.launch {
                when (cameraFacing) {
                    CameraFacing.Front -> {
                        lensFacing = CameraSelector.LENS_FACING_BACK
                        _cameraFacing.postValue(CameraFacing.Back)
                    }

                    CameraFacing.Back, null -> {
                        lensFacing = CameraSelector.LENS_FACING_FRONT
                        _cameraFacing.postValue(CameraFacing.Front)
                    }
                }
            }
        }
    }

    fun toggleFlash() {
        viewModelScope.launch {
            camera?.let { camera ->
                if (camera.cameraInfo.hasFlashUnit()) {
                    val isFlashOn = _flashOn.value ?: true

                    camera.cameraControl.enableTorch(!isFlashOn)
                    _flashOn.postValue(!isFlashOn)
                }
            }
        }
    }

    fun updateExposure(value: Float) {
        viewModelScope.launch {
            camera?.let { camera ->
                val range = camera.cameraInfo.exposureState.exposureCompensationRange
                val exposure = ((value * 2f) - 1f) * range.upper
                camera.cameraControl.setExposureCompensationIndex(exposure.toInt())
            }
        }
    }

    fun updateZoom(delta: Float) {
        viewModelScope.launch {
            camera?.let { camera ->
                val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
            }
        }
    }

    fun focus(point: MeteringPoint) {
        viewModelScope.launch {
            val action = FocusMeteringAction.Builder(
                point, FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
            )
                .setAutoCancelDuration(5, TimeUnit.SECONDS)
                .build()
            camera?.cameraControl?.startFocusAndMetering(action)
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startRecording() {
        videoCapture?.let { videoCapture ->
            val outputFileName = SaveToMediaStore.generateVideoFileName()

            val contentValues =
                SaveToMediaStore.generatePublicVideoContentValuesForVideo(outputFileName)

            val outputOptions = MediaStoreOutputOptions.Builder(
                context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ).setContentValues(contentValues).build()

            recording = videoCapture.output.prepareRecording(context, outputOptions)
                .start(cameraExecutor) { event ->
                    when (event) {
                        is VideoRecordEvent.Start -> {
                            playVideoStartSound()
                            _isCapturingVideo.postValue(true)
                        }

                        is VideoRecordEvent.Finalize -> {
                            playVideoStopSound()
                            _isCapturingVideo.postValue(false)
                            if (!event.hasError()) {
                                playVibrate()
                                _recordedVideo.postValue(event.outputResults.outputUri)
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    fun stopRecording() {
        videoCapture?.let {
            recording?.stop()
        }
    }

    private fun playVideoStartSound() {
        if (!canPlaySound) return

        shutterSound.play(MediaActionSound.START_VIDEO_RECORDING)
    }

    private fun playVideoStopSound() {
        if (!canPlaySound) return

        shutterSound.play(MediaActionSound.STOP_VIDEO_RECORDING)
    }

    private fun playVibrate() {
        if (!canVibrate) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(100)
        }
    }
}
