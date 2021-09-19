package ru.gaket.themoviedb.presentation.moviedetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MoviesScreen
import ru.gaket.themoviedb.databinding.MovieDetailsFragmentBinding
import ru.gaket.themoviedb.presentation.core.BaseFragment
import ru.gaket.themoviedb.presentation.moviedetails.viewmodel.MovieDetailsViewModel

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    lateinit var binding: MovieDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.argsState.observe(
            viewLifecycleOwner,
            {
                binding.tvMovieDetailsTitle.text = getString(R.string.movie_details_title, it.movieId, it.title)
            }
        )

        setupListeners()
    }

    private fun setupListeners() {
        binding.layMovieDetailsBack.setOnClickListener { navigateTo(MoviesScreen()) }
        binding.ivMovieDetailsBrowse.setOnClickListener { viewModel.onBrowseMovieCLick() }
    }

    companion object {

        fun create(movieId: Int, title: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("ARG_MOVIE_ID", movieId)
                putString("ARG_MOVIE_TITLE", title)
            }
        }
    }
}