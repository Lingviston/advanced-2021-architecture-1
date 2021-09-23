package ru.gaket.themoviedb.presentation.movies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.gaket.themoviedb.databinding.ItemMovieBinding
import ru.gaket.themoviedb.domain.movies.models.SearchMovie
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview

class MoviesAdapter(
    private val onMovieClick: (SearchMovie) -> Unit,
) : ListAdapter<SearchMovieWithMyReview, MovieViewHolder>(MoviesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position), onMovieClick)
    }
}

private class MoviesDiffCallback : DiffUtil.ItemCallback<SearchMovieWithMyReview>() {

    override fun areItemsTheSame(oldItem: SearchMovieWithMyReview, newItem: SearchMovieWithMyReview): Boolean =
        oldItem.movie.id == newItem.movie.id

    override fun areContentsTheSame(oldItem: SearchMovieWithMyReview, newItem: SearchMovieWithMyReview): Boolean =
        oldItem == newItem
}
