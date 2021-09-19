package ru.gaket.themoviedb.presentation.movies.view

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.ItemMovieBinding
import ru.gaket.themoviedb.domain.movies.models.SearchMovie

class MovieViewHolder(
	private val binding: ItemMovieBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val transformation: Transformation

    init {
        val dimension = itemView.resources.getDimension(R.dimen.cornerRad)
        val cornerRadius = dimension.toInt()
        transformation = RoundedCornersTransformation(cornerRadius, 0)
    }

    fun bind(searchMovie: SearchMovie, onMovieClick: (SearchMovie) -> Unit) {
        setName(searchMovie)
        setThumbnail(searchMovie)
        setClickListener(onMovieClick, searchMovie)
    }

    private fun setClickListener(
		onMovieClick: (SearchMovie) -> Unit,
		searchMovie: SearchMovie,
	) {
        itemView.setOnClickListener { onMovieClick(searchMovie) }
    }

    private fun setName(searchMovie: SearchMovie) {
        binding.movieName.text = searchMovie.title
    }

    private fun setThumbnail(searchMovie: SearchMovie) {
        Picasso.get()
            .load(searchMovie.thumbnail)
            .placeholder(R.drawable.ph_movie_grey_200)
            .error(R.drawable.ph_movie_grey_200)
            .transform(transformation)
            .fit()
            .centerCrop()
            .into(binding.movieThumbnail)
    }
}