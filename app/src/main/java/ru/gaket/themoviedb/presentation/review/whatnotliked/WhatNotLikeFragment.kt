package ru.gaket.themoviedb.presentation.review.whatnotliked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.RatingScreen
import ru.gaket.themoviedb.databinding.FragmentReviewTextBinding
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.EMPTY_FIELD
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.SUCCESS
import javax.inject.Inject

@AndroidEntryPoint
class WhatNotLikeFragment : Fragment(R.layout.fragment_review_text) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewTextBinding by viewBinding(FragmentReviewTextBinding::bind)

    private val viewModel: WhatNotLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvReviewMessage.setText(R.string.review_what_did_not_like)
            btnNext.setOnClickListener {
                viewModel.submitInfo(etReviewField.text.toString())
            }
        }

        viewModel.events.observe(viewLifecycleOwner) { reviewErrorField ->
            when (reviewErrorField) {
                EMPTY_FIELD -> Snackbar.make(
                    requireView(),
                    R.string.review_error_should_not_be_empty,
                    Snackbar.LENGTH_SHORT
                ).show()
                SUCCESS -> navigator.navigateTo(RatingScreen())
            }
        }
    }
}