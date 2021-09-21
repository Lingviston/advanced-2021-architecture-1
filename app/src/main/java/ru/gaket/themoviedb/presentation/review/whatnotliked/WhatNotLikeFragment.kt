package ru.gaket.themoviedb.presentation.review.whatnotliked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.ReviewScreen
import ru.gaket.themoviedb.databinding.FragmentReviewWhatDidLikeBinding
import javax.inject.Inject

//TODO [Vlad] Add Second layout or reuse first
@AndroidEntryPoint
class WhatNotLikeFragment : Fragment(R.layout.fragment_review_what_did_like) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewWhatDidLikeBinding
        get() = FragmentReviewWhatDidLikeBinding.bind(requireView())

    private val viewModel: WhatNotLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnNext.setOnClickListener {
                viewModel.submitInfo(etWhatDidYouLike.text.toString())
                //TODO [Vlad] Add validation and move navigation to concrete event
                navigator.navigateTo(ReviewScreen.RatingScreen)
            }
        }
    }

}