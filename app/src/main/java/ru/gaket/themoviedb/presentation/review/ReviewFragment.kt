package ru.gaket.themoviedb.presentation.review

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.FragmentReviewBinding
import ru.gaket.themoviedb.domain.movies.models.MovieId
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
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.reviewState.observe(viewLifecycleOwner) {
            when (it) {
                WHAT_LIKED -> childFragmentManager.commit {
                    replace(binding.containerReview.id, WhatLikeFragment(), null)
                }
                WHAT_NOT_LIKED -> childFragmentManager.commit {
                    replace(binding.containerReview.id, WhatNotLikeFragment(), null)
                    addToBackStack(null)
                }
                RATING -> childFragmentManager.commit {
                    replace(binding.containerReview.id, RatingFragment(), null)
                    addToBackStack(null)
                }
                END_STATE -> navigator.backTo(MovieDetailsScreen.TAG)
            }
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