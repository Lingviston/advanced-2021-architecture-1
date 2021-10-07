package ru.gaket.themoviedb.presentation.moviedetails.view

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.ItemReviewBinding
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview

class ReviewViewHolder(
    private val binding: ItemReviewBinding,
    private val onReviewClick: (MovieDetailsReview) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(review: MovieDetailsReview) = with(binding) {
        val context = itemView.context

        val isAddReviewView = (review is MovieDetailsReview.Add)

        gReviewContent.isVisible = isAddReviewView.not()
        gReviewAdd.isVisible = isAddReviewView

        when (review) {
            is MovieDetailsReview.Add -> setupAddReviewView(context, review)
            is MovieDetailsReview.Existing -> setupExistingReviewView(context, review)
        }

        cvReviewCard.setOnClickListener { onReviewClick(review) }
    }

    private fun setupAddReviewView(context: Context, review: MovieDetailsReview.Add) {
        binding.tvReviewAddLabelLabel.text = context.getTextForAddReviewButton(review.isAuthorized)
    }

    private fun setupExistingReviewView(context: Context, review: MovieDetailsReview.Existing) = with(binding) {
        tvReviewUserName.text = when (review) {
            is MovieDetailsReview.Existing.My -> context.getString(R.string.review_my_review)
            is MovieDetailsReview.Existing.Someone -> review.info.author.value
        }

        rbReviewRating.rating = review.review.rating.starsCount.toFloat()
        tvReviewLiked.text = review.review.liked
        tvReviewDisliked.text = review.review.disliked
    }
}

private fun Context.getTextForAddReviewButton(isAuthorized: Boolean): String {
    @StringRes val strResId = if (isAuthorized) {
        R.string.review_add_label
    } else {
        R.string.authiorize_to_add_review_label
    }

    return this.getString(strResId)
}