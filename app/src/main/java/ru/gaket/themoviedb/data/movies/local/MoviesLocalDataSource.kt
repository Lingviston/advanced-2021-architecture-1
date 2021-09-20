package ru.gaket.themoviedb.data.movies.local

import javax.inject.Inject

interface MoviesLocalDataSource {
    suspend fun getById(id: Int): MovieEntity?
    suspend fun insertAll(searchedMovies: List<SearchMovieEntity>)
    suspend fun update(movie: MovieEntity)
    suspend fun deleteAll()
    suspend fun deleteById(id: Int)
}

class MoviesLocalDataSourceImpl @Inject constructor(
	private val moviesDao: MoviesDao,
) : MoviesLocalDataSource {

    override suspend fun getById(id: Int): MovieEntity? = moviesDao.getById(id)

    override suspend fun insertAll(searchedMovies: List<SearchMovieEntity>) {
        moviesDao.insertAll(searchedMovies)
    }

    override suspend fun update(movie: MovieEntity) {
        moviesDao.update(movie)
    }

    override suspend fun deleteAll() {
        moviesDao.deleteAll()
    }

    override suspend fun deleteById(id: Int) {
        moviesDao.deleteById(id)
    }
}