package ru.gaket.themoviedb.presentation.review.whatnotliked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.FragmentReviewTextBinding
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.EMPTY_FIELD
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.SUCCESS
import ru.gaket.themoviedb.presentation.review.ReviewViewModel
import ru.gaket.themoviedb.util.showSnackbar

@AndroidEntryPoint
class WhatNotLikeFragment : Fragment(R.layout.fragment_review_text) {

    private val binding: FragmentReviewTextBinding by viewBinding(FragmentReviewTextBinding::bind)

    private val viewModel: WhatNotLikeViewModel by viewModels()

    private val sharedViewModel: ReviewViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvReviewMessage.setText(R.string.review_what_did_not_like)
            btnNext.setOnClickListener {
                viewModel.submitInfo(etReviewField.text.toString())
            }
        }

        viewModel.events.observe(viewLifecycleOwner, ::handleState)

        viewModel.initialValue.observe(viewLifecycleOwner) { initialValue ->
            binding.etReviewField.setText(initialValue)
        }
    }

    private fun handleState(reviewErrorField: ReviewFieldEvent) {
        when (reviewErrorField) {
            EMPTY_FIELD -> requireView().showSnackbar(R.string.review_error_should_not_be_empty)
            SUCCESS -> sharedViewModel.nextState()
        }
    }
}
