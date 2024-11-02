package es.oliva.samuel.camerax_mvvm.ui.camera.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.oliva.samuel.camerax_mvvm.ui.camera.viewModel.CameraViewModel
import es.oliva.samuel.camerax_mvvm.ui.common.camera.AbstractCameraFragment

@AndroidEntryPoint
class CameraFragment : AbstractCameraFragment() {
    override val viewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
            .also { viewModel.onCreate() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.didInsertPage.observe(viewLifecycleOwner) { didInsertPage ->
            if (didInsertPage) findNavController().navigateUp()
        }
    }

    override fun onImageConfirmationAction() {
        viewModel.insertCapturedPage()
    }
}
