package ru.gaket.themoviedb.presentation.review.rating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.FragmentReviewRatingBinding
import kotlin.math.roundToInt

@AndroidEntryPoint
class RatingFragment : Fragment(R.layout.fragment_review_rating) {

    private val binding: FragmentReviewRatingBinding
        get() = FragmentReviewRatingBinding.bind(requireView())

    private val viewModel: RatingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rbRateMovie.setOnRatingBarChangeListener { _, rating, _ ->
                viewModel.changeRating(rating.roundToInt())
            }
            btnSubmit.setOnClickListener {
                viewModel.submit()
            }
        }
    }

}