package ru.gaket.themoviedb.presentation.review

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.FragmentReviewBinding
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.END_STATE
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.RATING
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_LIKED
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_NOT_LIKED
import ru.gaket.themoviedb.presentation.review.rating.RatingFragment
import ru.gaket.themoviedb.presentation.review.whatliked.WhatLikeFragment
import ru.gaket.themoviedb.presentation.review.whatnotliked.WhatNotLikeFragment
import javax.inject.Inject

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewBinding by viewBinding(FragmentReviewBinding::bind)

    private val viewModel: ReviewViewModel by viewModels()

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.previousState()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.reviewState.observe(viewLifecycleOwner) {
            val containerId = binding.containerReview.id
            when (it) {
                WHAT_LIKED -> childFragmentManager.commit {
                    replace(containerId, WhatLikeFragment())
                }
                WHAT_NOT_LIKED -> childFragmentManager.commit {
                    replace(containerId, WhatNotLikeFragment())
                }
                RATING -> childFragmentManager.commit {
                    replace(containerId, RatingFragment())
                }
                END_STATE -> navigator.backTo(MovieDetailsScreen.TAG)
            }
        }

        viewModel.currentReview.observe(viewLifecycleOwner) { movieToReview ->
            binding.cvReviewSummary.isVisible = movieToReview != null
            movieToReview?.let { (movie, review) -> binding.bind(movie, review) }
        }
    }

    private fun FragmentReviewBinding.bind(movie: Movie, review: ReviewFormModel) {
        with(containerReviewSummary) {
            Picasso.get()
                .load(movie.thumbnail)
                .placeholder(R.drawable.ph_movie_grey_200)
                .error(R.drawable.ph_movie_grey_200)
                .fit()
                .centerCrop()
                .into(ivMovieThumbnail)

            tvRateMovieMessage.text = getString(R.string.review_rate_placeholder, movie.title)

            tvWhatLike.isVisible = review.whatLiked != null
            tvWhatNotLike.isVisible = review.whatDidNotLike != null

            tvWhatLike.text = getString(R.string.review_liked_placeholder, review.whatLiked)
            tvWhatNotLike.text = getString(R.string.review_not_liked_placeholder, review.whatDidNotLike)
        }
    }

    companion object {

        fun newInstance(movieId: MovieId): ReviewFragment {
            return ReviewFragment().apply {
                arguments = bundleOf(ReviewViewModel.ARG_MOVIE_ID to movieId)
            }
        }
    }
}