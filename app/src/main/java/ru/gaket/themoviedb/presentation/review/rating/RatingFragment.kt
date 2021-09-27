package ru.gaket.themoviedb.presentation.review.rating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.R.string
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.FragmentReviewRatingBinding
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent.Success
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent.UnknownError
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent.UserNotSignedInError
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewEvent.ZeroRatingError
import ru.gaket.themoviedb.presentation.review.rating.RatingViewModel.ReviewState
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class RatingFragment : Fragment(R.layout.fragment_review_rating) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentReviewRatingBinding by viewBinding(FragmentReviewRatingBinding::bind)

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
        Timber.d("Event received: $reviewEvent")
        when (reviewEvent) {
            UnknownError, UserNotSignedInError -> {
                binding.rbRateMovie.isEnabled = true
                binding.btnSubmit.isEnabled = true
                Snackbar.make(
                    requireView(),
                    string.review_error_unknown,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            ZeroRatingError -> {
                Snackbar.make(
                    requireView(),
                    string.review_error_zero_rating,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            Success -> {
                navigator.backTo(MovieDetailsScreen.TAG)
            }
        }
    }

    private fun processReviewState(reviewState: ReviewState) {
        val isEnabled = when (reviewState) {
            is ReviewState.Loading -> false
            is ReviewState.Idle -> true
        }

        binding.rbRateMovie.isEnabled = isEnabled
        binding.btnSubmit.isEnabled = isEnabled
    }
}