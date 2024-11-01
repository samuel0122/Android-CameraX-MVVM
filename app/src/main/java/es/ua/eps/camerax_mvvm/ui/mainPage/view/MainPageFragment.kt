package es.ua.eps.camerax_mvvm.ui.mainPage.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import es.ua.eps.camerax_mvvm.R
import es.ua.eps.camerax_mvvm.databinding.FragmentMainPageBinding
import es.ua.eps.camerax_mvvm.ui.mainPage.viewModel.MainPageViewModel

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private val viewModel: MainPageViewModel by viewModels()

    private lateinit var binding: FragmentMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater)

        return binding.root
    }
}