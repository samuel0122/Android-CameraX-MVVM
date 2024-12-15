package es.oliva.samuel.camerax_mvvm.ui.mainPage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.oliva.samuel.camerax_mvvm.R
import es.oliva.samuel.camerax_mvvm.core.eMediaType
import es.oliva.samuel.camerax_mvvm.databinding.FragmentMainPageBinding
import es.oliva.samuel.camerax_mvvm.ui.mainPage.viewModel.MainPageViewModel

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private val viewModel: MainPageViewModel by viewModels()

    private lateinit var binding: FragmentMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater)

        binding.btnOpenCamera.setOnClickListener {
            findNavController().navigate(
                MainPageFragmentDirections.actionMainPageFragmentToOpenCameraOptionsDialog()
            )
        }

        binding.ivThumbnail.setOnClickListener {
            viewModel.lastSavedMedia.value?.let { lastSavedMediaItem ->
                when (lastSavedMediaItem.mediaType) {
                    eMediaType.Video -> {
                        findNavController().navigate(
                            MainPageFragmentDirections.actionMainPageFragmentToVideoPreviewFragment(
                                videoUri = lastSavedMediaItem.mediaUri.toString()
                            )
                        )
                    }

                    eMediaType.Picture -> {
                        findNavController().navigate(
                            MainPageFragmentDirections.actionMainPageFragmentToPhotoPreviewFragment(
                                photo = viewModel.lastSavedMediaThumbnail.value!!
                            )
                        )
                    }
                }
            }
        }

        binding.btnGitHub.setOnClickListener {}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lastSavedMediaThumbnail.observe(viewLifecycleOwner) { bitmap ->
            if (bitmap != null) binding.ivThumbnail.setImageBitmap(bitmap)
            else binding.ivThumbnail.setImageResource(R.drawable.ic_launcher_background)
        }

        viewModel.lastSavedMedia.observe(viewLifecycleOwner) { savedMedia ->
            savedMedia?.let {
                when (savedMedia.mediaType) {
                    eMediaType.Video -> binding.ivVideoPlayIcon.visibility = View.VISIBLE
                    eMediaType.Picture -> binding.ivVideoPlayIcon.visibility = View.GONE
                }
            }
        }
    }
}
