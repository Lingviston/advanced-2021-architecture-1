package ru.gaket.themoviedb.presentation.review.whatliked

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.ReviewScreen
import ru.gaket.themoviedb.databinding.FragmentReviewWhatDidLikeBinding
import ru.gaket.themoviedb.domain.movies.models.MovieId
import javax.inject.Inject

@AndroidEntryPoint
class WhatLikeFragment : Fragment(R.layout.fragment_review_what_did_like) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewWhatDidLikeBinding
        get() = FragmentReviewWhatDidLikeBinding.bind(requireView())

    private val viewModel: WhatLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnNext.setOnClickListener {
                viewModel.submitInfo(etWhatDidYouLike.text.toString())
                //TODO [Vlad] Add validation and move navigation to concrete event
                navigator.navigateTo(ReviewScreen.NotLikedScreen)
            }
        }
    }

    companion object {
        fun newInstance(movieId: MovieId): WhatLikeFragment {
            return WhatLikeFragment().apply {
                arguments = bundleOf(WhatLikeViewModel.ARG_MOVIE_ID to movieId)
            }
        }
    }

}