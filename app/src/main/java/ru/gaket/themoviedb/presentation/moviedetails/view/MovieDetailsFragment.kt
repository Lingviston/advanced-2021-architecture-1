package ru.gaket.themoviedb.presentation.moviedetails.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MoviesScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.ReviewFlow
import ru.gaket.themoviedb.databinding.MovieDetailsFragmentBinding
import ru.gaket.themoviedb.presentation.moviedetails.viewmodel.MovieDetailsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val binding by viewBinding(MovieDetailsFragmentBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.argsState.observe(
            viewLifecycleOwner,
            { movieDetailState ->
                binding.btnReviewMovie.isEnabled = true
                binding.btnReviewMovie.setOnClickListener {
                    navigator.navigateTo(ReviewFlow.LikedScreen(movieDetailState.movieId))
                }
                binding.tvMovieDetailsTitle.text = getString(
                    R.string.movie_details_title,
                    movieDetailState.movieId,
                    movieDetailState.title
                )
            }
        )

        setupListeners()
    }

    private fun setupListeners() {
        binding.layMovieDetailsBack.setOnClickListener { navigator.navigateTo(MoviesScreen()) }
        binding.ivMovieDetailsBrowse.setOnClickListener { viewModel.onBrowseMovieCLick() }
    }

    companion object {

        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
        const val ARG_MOVIE_TITLE = "ARG_MOVIE_TITLE"

        fun newInstance(movieId: Long, title: String): MovieDetailsFragment = MovieDetailsFragment()
            .apply {
                arguments = bundleOf(
                    ARG_MOVIE_ID to movieId,
                    ARG_MOVIE_TITLE to title,
                )
            }
    }
}
