package ru.gaket.themoviedb.presentation.moviedetails.view

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.ItemReviewBinding
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview.MyReview
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview.SomeoneReview

class ReviewViewHolder(
    private val binding: ItemReviewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        review: MovieDetailsReview,
        onReviewClick: () -> Unit,
        onAddReviewClick: () -> Unit,
    ) = with(binding) {
        val isAddReviewView = review is MyReview && review.isPosted.not()

        gReviewContent.isVisible = isAddReviewView.not()
        gReviewAdd.isVisible = isAddReviewView

        if (isAddReviewView) {
            cvReviewCard.setOnClickListener { onAddReviewClick.invoke() }
        } else {
            cvReviewCard.setOnClickListener { onReviewClick.invoke() }
            setupReviewView(review)
        }
    }

    private fun setupReviewView(review: MovieDetailsReview) = with(binding) {
        tvReviewUserName.text = when (review) {
            is MyReview -> tvReviewUserName.context.getString(R.string.review_my_review)
            is SomeoneReview -> review.userName
        }

        rbReviewRating.rating = review.review?.rating?.starsCount?.toFloat() ?: 0.0f
        tvReviewLiked.text = review.review?.liked.orEmpty()
        tvReviewDisliked.text = review.review?.disliked.orEmpty()
    }
}
