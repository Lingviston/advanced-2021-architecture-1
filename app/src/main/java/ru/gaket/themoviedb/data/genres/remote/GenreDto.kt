package ru.gaket.themoviedb.data.genres.remote

import com.google.gson.annotations.SerializedName

/**
 * Class of Genre coming from the api
 */
data class GenreDto(
	@SerializedName("id")
	val id: Int,

	@SerializedName("name")
	val name: String,
)