package ru.gaket.themoviedb.presentation.movies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.gaket.themoviedb.databinding.ItemMovieBinding
import ru.gaket.themoviedb.domain.movies.models.SearchMovie

class MoviesAdapter(
    private val onMovieClick: (SearchMovie) -> Unit,
) : ListAdapter<SearchMovie, MovieViewHolder>(MoviesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position), onMovieClick)
    }
}

private class MoviesDiffCallback : DiffUtil.ItemCallback<SearchMovie>() {

    override fun areItemsTheSame(oldItem: SearchMovie, newItem: SearchMovie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SearchMovie, newItem: SearchMovie): Boolean =
        oldItem == newItem
}
