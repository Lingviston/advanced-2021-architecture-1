package ru.gaket.themoviedb.data.movies.remote

import com.google.gson.annotations.SerializedName

/**
 * Class of Movie coming from the api
 */
data class SearchMovieDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("poster_path")
    val posterPath: String?,
)