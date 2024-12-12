package es.oliva.samuel.camerax_mvvm.ui.preview.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.oliva.samuel.camerax_mvvm.databinding.FragmentVideoPreviewBinding
import es.oliva.samuel.camerax_mvvm.ui.preview.viewModel.VideoPreviewViewModel

@AndroidEntryPoint
class VideoPreviewFragment : Fragment() {
    private val viewModel: VideoPreviewViewModel by viewModels()
    private val args: VideoPreviewFragmentArgs by navArgs()

    private lateinit var binding: FragmentVideoPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.videoUri.toUri())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoPreviewBinding.inflate(inflater)

        binding.apply {
            btnConfirmPhoto.setOnClickListener { viewModel.acceptRecordedVideo() }
            btnDiscardPhoto.setOnClickListener { viewModel.rejectRecordedVideo() }

            vvPreview.player = ExoPlayer.Builder(requireContext()).build()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recordedVideo.observe(viewLifecycleOwner) { recordedVideo ->
            binding.vvPreview.player?.apply {
                setMediaItem(MediaItem.fromUri(recordedVideo))
                prepare()
                playWhenReady = true
            }
        }

        viewModel.didAcceptVideo.observe(viewLifecycleOwner) { didAcceptVideo ->
            if (didAcceptVideo) findNavController().navigate(
                VideoPreviewFragmentDirections.actionVideoPreviewFragmentToMainPageFragment()
            )
        }

        viewModel.didRejectVideo.observe(viewLifecycleOwner) { didRejectVideo ->
            if (didRejectVideo) findNavController().navigateUp()
        }
    }
}
