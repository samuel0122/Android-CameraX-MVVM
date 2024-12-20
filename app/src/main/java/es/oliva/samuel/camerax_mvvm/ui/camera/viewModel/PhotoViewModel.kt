package es.oliva.samuel.camerax_mvvm.ui.camera.viewModel

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.AudioManager
import android.media.MediaActionSound
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.oliva.samuel.camerax_mvvm.ui.camera.CameraFacing
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val audioManager: AudioManager,
    private val vibrator: Vibrator
) : ViewModel() {
    private val _flashOn = MutableLiveData<Boolean>()
    private val _isCapturingImage = MutableLiveData<Boolean>()
    private val _cameraFacing = MutableLiveData<CameraFacing>()
    private val _pictureBitmap = MutableLiveData<Bitmap?>()

    val flashOn: LiveData<Boolean> get() = _flashOn
    val isCapturingImage: LiveData<Boolean> get() = _isCapturingImage
    val cameraFacing: LiveData<CameraFacing> get() = _cameraFacing
    val pictureBitmap: LiveData<Bitmap?> get() = _pictureBitmap

    private var _cameraProvider: ProcessCameraProvider? = null
    private var _camera: Camera? = null

    val camera: Camera? get() = _camera
    val cameraProvider: ProcessCameraProvider? get() = _cameraProvider

    var preview: Preview? = null
    var imageCapture: ImageCapture? = null
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
        shutterSound.load(MediaActionSound.SHUTTER_CLICK)
        Log.e("AbstractPhotoViewModel", "onCreate")
    }

    fun onDestroy() {
        cameraExecutor.shutdown()
        shutterSound.release()
        cameraProvider?.unbindAll()
        Log.e("AbstractPhotoViewModel", "onStopCamera")
    }

    fun onStartCamera(rotation: Int) {
        viewModelScope.launch {
            buildImageCapture(rotation)

            buildPreview(rotation)

            _flashOn.postValue(flashOn.value ?: false)
            _isCapturingImage.postValue(isCapturingImage.value ?: false)
            _cameraFacing.postValue(cameraFacing.value ?: CameraFacing.Back)

            Log.e("AbstractPhotoViewModel", "onStartCamera")
        }
    }

    fun onStopCamera() {
        imageCapture = null
        preview = null
        _camera = null
        _pictureBitmap.postValue(null)

        Log.e("AbstractPhotoViewModel", "onStopCamera")
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

    private fun buildImageCapture(rotation: Int) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    aspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
    }

    fun bindCameraUseCases(viewLifecycleOwner: LifecycleOwner) {
        cameraProvider?.unbindAll()

        // Bind use cases to camera
        val camera = cameraProvider?.bindToLifecycle(
            viewLifecycleOwner, cameraSelector, preview, imageCapture
        )

        _camera = camera
    }

    fun setCameraProvider(cameraProvider: ProcessCameraProvider) {
        _cameraProvider = cameraProvider
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

    fun takePicture() {
        viewModelScope.launch {
            _isCapturingImage.postValue(true)
            playShutterSound()
            playVibrate()

            imageCapture?.takePicture(cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        var bitmap = image.toBitmap()

                        if (image.imageInfo.rotationDegrees != 0) {
                            val matrix = Matrix()
                            matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
                            bitmap = Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                            )
                        }

                        _pictureBitmap.postValue(bitmap)
                        _isCapturingImage.postValue(false)

                        image.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        _isCapturingImage.postValue(false)
                        Log.e("CameraCapture", "Error al capturar imagen: ${exception.message}")
                    }
                })
        }
    }

    private fun playShutterSound() {
        if (!canPlaySound) return

        shutterSound.play(MediaActionSound.SHUTTER_CLICK)
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
