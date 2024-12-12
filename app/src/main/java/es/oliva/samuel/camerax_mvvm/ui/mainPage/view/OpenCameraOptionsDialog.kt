package es.oliva.samuel.camerax_mvvm.ui.mainPage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import es.oliva.samuel.camerax_mvvm.databinding.DialogOpenCameraOptionsBinding

@AndroidEntryPoint
class OpenCameraOptionsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogOpenCameraOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOpenCameraOptionsBinding.inflate(inflater)

        binding.btnTakePhoto.setOnClickListener {
            findNavController().navigate(
                OpenCameraOptionsDialogDirections.actionOpenCameraOptionsDialogToPhotoFragment()
            )
        }

        binding.btnRecordVideo.setOnClickListener {
            findNavController().navigate(
                OpenCameraOptionsDialogDirections.actionOpenCameraOptionsDialogToVideoFragment()
            )
        }

        return binding.root
    }
}
