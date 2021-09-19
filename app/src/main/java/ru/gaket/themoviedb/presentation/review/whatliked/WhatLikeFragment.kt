package ru.gaket.themoviedb.presentation.review.whatliked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.FragmentReviewWhatDidLikeBinding
import ru.gaket.themoviedb.presentation.review.ReviewNavigator
import javax.inject.Inject

@AndroidEntryPoint
class WhatLikeFragment : Fragment(R.layout.fragment_review_what_did_like) {

    //TODO move navigator to activity
    @Inject
    lateinit var navigator: ReviewNavigator

    private val binding: FragmentReviewWhatDidLikeBinding
        get() = FragmentReviewWhatDidLikeBinding.bind(requireView())

    private val viewModel: WhatLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator.init(requireActivity().supportFragmentManager)

        with(binding) {
            btnNext.setOnClickListener {
                viewModel.submitInfo(etWhatDidYouLike.text.toString())
            }
        }
    }

}