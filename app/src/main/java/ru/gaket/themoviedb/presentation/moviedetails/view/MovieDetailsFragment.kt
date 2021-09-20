package ru.gaket.themoviedb.presentation.moviedetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    // todo: [Sergey] clean up binding in onDestroyView
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

        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
        const val ARG_MOVIE_TITLE = "ARG_MOVIE_TITLE"

        fun newInstance(movieId: Long, title: String) = MovieDetailsFragment()
            .apply {
                arguments = bundleOf(
                    ARG_MOVIE_ID to movieId,
                    ARG_MOVIE_TITLE to title,
                )
            }
    }
}
