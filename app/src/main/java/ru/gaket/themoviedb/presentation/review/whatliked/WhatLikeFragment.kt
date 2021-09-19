package ru.gaket.themoviedb.presentation.review.whatliked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.FragmentReviewWhatDidLikeBinding

@AndroidEntryPoint
class WhatLikeFragment : Fragment(R.layout.fragment_review_what_did_like) {

    private val binding: FragmentReviewWhatDidLikeBinding
        get() = FragmentReviewWhatDidLikeBinding.bind(requireView())

    private val viewModel: WhatLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnNext.setOnClickListener {
                viewModel.submitInfo(etWhatDidYouLike.text.toString())
            }
        }
    }

}