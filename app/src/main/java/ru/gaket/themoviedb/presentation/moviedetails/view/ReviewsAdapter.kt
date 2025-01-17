package ru.gaket.themoviedb.presentation.moviedetails.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.gaket.themoviedb.databinding.ItemReviewBinding
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview

class ReviewsAdapter(
    private val onReviewClick: (MovieDetailsReview) -> Unit,
) : ListAdapter<MovieDetailsReview, ReviewViewHolder>(ReviewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(layoutInflater, parent, false)

        return ReviewViewHolder(binding, onReviewClick)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) =
        holder.bind(getItem(position))
}

private class ReviewsDiffCallback : DiffUtil.ItemCallback<MovieDetailsReview>() {

    override fun areItemsTheSame(oldItem: MovieDetailsReview, newItem: MovieDetailsReview): Boolean =
        (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: MovieDetailsReview, newItem: MovieDetailsReview): Boolean =
        (oldItem == newItem)
}