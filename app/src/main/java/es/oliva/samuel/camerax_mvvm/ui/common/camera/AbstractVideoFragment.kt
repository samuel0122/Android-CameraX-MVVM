package es.oliva.samuel.camerax_mvvm.ui.common.camera

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import es.oliva.samuel.camerax_mvvm.R
import es.oliva.samuel.camerax_mvvm.core.utils.Permissions
import es.oliva.samuel.camerax_mvvm.databinding.FragmentAbstractVideoBinding

abstract class AbstractVideoFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    protected abstract val viewModel: AbstractVideoViewModel

    protected lateinit var binding: FragmentAbstractVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate()

        startCamera()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAbstractVideoBinding.inflate(inflater)

        binding.apply {
            btnCloseCamera.setOnClickListener { findNavController().navigateUp() }
            btnFlipCamera.setOnClickListener { viewModel.flipCamera() }
            btnToggleFlash.setOnClickListener { viewModel.toggleFlash() }
            btnVideoRecord.setOnClickListener {
                viewModel.isCapturingVideo.value?.let { isCapturingVideo ->
                    if (isCapturingVideo) viewModel.stopRecording()
                    else viewModel.startRecording()
                }
            }

            btnDiscardPhoto.setOnClickListener { viewModel.discardRecordedVideo() }
            btnConfirmPhoto.setOnClickListener { viewModel.acceptRecordedVideo() }

            vvPreview.player = ExoPlayer.Builder(requireContext()).build()

            clLive.visibility = View.GONE
            clPreview.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.cameraFacing.observe(viewLifecycleOwner) { _ ->
            bindCameraUserCases()
        }

        viewModel.flashOn.observe(viewLifecycleOwner) { isFlashOn ->
            if (isFlashOn) binding.btnToggleFlash.setImageResource(R.drawable.ic_flash_on)
            else binding.btnToggleFlash.setImageResource(R.drawable.ic_flash_off)
        }

        viewModel.recordedVideo.observe(viewLifecycleOwner) { recordedVideoUri ->
            binding.vvPreview.player?.apply {
                setMediaItem(MediaItem.fromUri(recordedVideoUri))
                prepare()
                playWhenReady = true
            }
        }

        viewModel.cameraState.observe(viewLifecycleOwner) { cameraState ->
            cameraState?.let {
                binding.apply {
                    when (cameraState) {
                        CameraState.Live -> {
                            clLive.visibility = View.VISIBLE
                            clPreview.visibility = View.GONE
                        }

                        CameraState.ImageCaptured -> {
                            clLive.visibility = View.GONE
                            clPreview.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.isCapturingVideo.observe(viewLifecycleOwner) { isCapturingImage ->
            if (isCapturingImage) binding.btnVideoRecord.setImageResource(R.drawable.ic_recording)
            else binding.btnVideoRecord.setImageResource(R.drawable.ic_record)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(), getText(R.string.camera_permission_granted), Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity())
                .title(R.string.camera_permission_request_title)
                .rationale(R.string.camera_permission_request).positiveButtonText(R.string.ok)
                .negativeButtonText(R.string.cancel).build().show()
            findNavController().navigateUp()
        } else {
            Permissions.requestVideoPermissions(this)
            findNavController().navigateUp()
        }
    }

    @AfterPermissionGranted(Permissions.REQUEST_CODE_VIDEO)
    private fun startCamera() {
        if (EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.CAMERA)) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

            cameraProviderFuture.addListener({
                viewModel.setCameraProvider(cameraProviderFuture.get())

                viewModel.onStartCamera(binding.pvCamera.display.rotation)

                viewModel.preview?.apply {
                    surfaceProvider = binding.pvCamera.surfaceProvider
                }
            }, ContextCompat.getMainExecutor(requireContext()))
        } else {
            Permissions.requestVideoPermissions(this)
        }
    }

    private fun bindCameraUserCases() {
        try {
            viewModel.cameraProvider?.let { cameraProvider ->
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    viewModel.cameraSelector,
                    viewModel.preview,
                    viewModel.videoCapture
                )

                viewModel.setCamera(camera)

                setUpExposure()
                setUpZoomTapToFocusAndExposure()
            }

        } catch (_: Exception) {
        }
    }

    private fun setUpExposure() {
        viewModel.updateExposure(binding.sbExposure.progress.toFloat() / binding.sbExposure.max.toFloat())
        binding.sbExposure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.let {
                    viewModel.updateExposure(progress.toFloat() / seekBar.max.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setUpZoomTapToFocusAndExposure() {
        // Configuración del detector de gestos para el zoom
        val scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    viewModel.updateZoom(detector.scaleFactor)
                    return true
                }
            })

        // Configuración del detector de gestos para el enfoque
        val gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    // Mostrar círculo de enfoque
                    binding.focusCircleView.setRectPoint(e.x, e.y)
                    binding.focusCircleView.invalidate()

                    // Focus
                    val factory = binding.pvCamera.meteringPointFactory
                    val point = factory.createPoint(e.x, e.y)
                    viewModel.focus(point)

                    return true
                }
            })

        // Configuración del enfoque táctil y el zoom
        binding.pvCamera.setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)
            if (!scaleGestureDetector.isInProgress) {
                gestureDetector.onTouchEvent(event)
            }
            view.performClick()
            true
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
