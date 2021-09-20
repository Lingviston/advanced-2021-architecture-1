package ru.gaket.themoviedb.data.movies.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Movies api of themoviedb.org
 */
interface MoviesApi {

    @GET("search/movie")
    suspend fun searchMovie(
		@Query("query") query: String,
		@Query("page") page: Int = 1,
	): SearchMovieResponse

    @GET("/movie/{movie_id}")
    suspend fun getMovieDetails(
		@Path("movie_id") id: Int
    ): DetailsMovieDto
}