package ru.gaket.themoviedb.presentation.review.whatliked

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.NotLikedScreen
import ru.gaket.themoviedb.databinding.FragmentReviewTextBinding
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.EMPTY_FIELD
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.SUCCESS
import ru.gaket.themoviedb.util.showSnackbar
import javax.inject.Inject

@AndroidEntryPoint
class WhatLikeFragment : Fragment(R.layout.fragment_review_text) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewTextBinding by viewBinding(FragmentReviewTextBinding::bind)

    private val viewModel: WhatLikeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvReviewMessage.setText(R.string.review_what_did_you_like)
            btnNext.setOnClickListener {
                viewModel.submitInfo(etReviewField.text.toString())
            }
        }

        viewModel.events.observe(viewLifecycleOwner) { reviewErrorField ->
            when (reviewErrorField) {
                EMPTY_FIELD -> requireView().showSnackbar(R.string.review_error_should_not_be_empty)
                SUCCESS -> navigator.navigateTo(NotLikedScreen())
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