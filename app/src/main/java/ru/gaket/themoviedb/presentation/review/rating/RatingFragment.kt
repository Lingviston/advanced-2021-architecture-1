package ru.gaket.themoviedb.presentation.review.rating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.FragmentReviewRatingBinding
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewState
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class RatingFragment : Fragment(R.layout.fragment_review_rating) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewRatingBinding
        get() = FragmentReviewRatingBinding.bind(requireView())

    private val viewModel: RatingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            viewModel.submit(binding.rbRateMovie.rating.roundToInt())
        }

        viewModel.reviewEvent.observe(viewLifecycleOwner, ::processReviewEvent)
        viewModel.reviewState.observe(viewLifecycleOwner, ::processReviewState)
    }

    private fun processReviewEvent(reviewEvent: ReviewEvent) {
        when (reviewEvent) {
            ReviewEvent.UnknownError -> {
                binding.rbRateMovie.isEnabled = true
                binding.btnSubmit.isEnabled = true
                Snackbar.make(
                    requireView(),
                    R.string.review_error_unknown,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            ReviewEvent.ZeroRatingError -> {
                Snackbar.make(
                    requireView(),
                    R.string.review_error_zero_rating,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            ReviewEvent.Success -> navigator.backTo(MovieDetailsScreen.TAG)
        }
    }

    private fun processReviewState(reviewState: ReviewState) {
        fun setViewState(isEnabled: Boolean) {
            binding.rbRateMovie.isEnabled = isEnabled
            binding.btnSubmit.isEnabled = isEnabled
        }
        when (reviewState) {
            is ReviewState.Loading -> setViewState(false)
            is ReviewState.Idle -> setViewState(true)
        }
    }
}