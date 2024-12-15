package es.oliva.samuel.camerax_mvvm.ui.preview.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.oliva.samuel.camerax_mvvm.databinding.FragmentPhotoPreviewBinding
import es.oliva.samuel.camerax_mvvm.ui.preview.viewModel.PhotoPreviewViewModel

@AndroidEntryPoint
class PhotoPreviewFragment : Fragment() {
    private val viewModel: PhotoPreviewViewModel by viewModels()
    private val args: PhotoPreviewFragmentArgs by navArgs()

    private lateinit var binding: FragmentPhotoPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.photo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoPreviewBinding.inflate(inflater)

        binding.apply {
            btnConfirmPhoto.setOnClickListener { viewModel.acceptCapturedPicture() }
            btnDiscardPhoto.setOnClickListener { viewModel.rejectCapturedPicture() }

            val showButtons = args.showActionButtons
            btnConfirmPhoto.visibility = if (showButtons) View.VISIBLE else View.GONE
            btnDiscardPhoto.visibility = if (showButtons) View.VISIBLE else View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photoBitmap.observe(viewLifecycleOwner) { photoBitmap ->
            binding.ivPhoto.setImageBitmap(photoBitmap)
        }

        viewModel.didAcceptPhoto.observe(viewLifecycleOwner) { didAcceptPhoto ->
            if (didAcceptPhoto) findNavController().navigate(
                PhotoPreviewFragmentDirections.actionPhotoPreviewFragmentToMainPageFragment()
            )
        }

        viewModel.didRejectPhoto.observe(viewLifecycleOwner) { didRejectPhoto ->
            if (didRejectPhoto) findNavController().navigateUp()
        }
    }
}
