package ru.gaket.themoviedb.data.review.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.gaket.themoviedb.domain.movies.models.MovieId
import javax.inject.Inject

interface MyReviewsLocalDataSource {

    fun observeMovieById(movieId: MovieId): Flow<MyReviewEntity?>
    suspend fun getByMovieIds(movieIds: List<MovieId>): List<MyReviewEntity>
    suspend fun insertAll(myReviews: List<MyReviewEntity>)
    suspend fun insert(myReview: MyReviewEntity)
    suspend fun deleteAll()
}

class MyReviewsLocalDataSourceImpl @Inject constructor(
    private val myReviewsDao: MyReviewsDao,
) : MyReviewsLocalDataSource {

    override fun observeMovieById(movieId: MovieId): Flow<MyReviewEntity?> =
        myReviewsDao.observeMoviesById(movieId)
            .map { list -> list.firstOrNull() }
            .distinctUntilChanged()

    override suspend fun getByMovieIds(movieIds: List<MovieId>): List<MyReviewEntity> =
        myReviewsDao.getByMovieIds(movieIds)

    override suspend fun insertAll(myReviews: List<MyReviewEntity>) =
        myReviewsDao.insertAll(myReviews)

    override suspend fun insert(myReview: MyReviewEntity) =
        myReviewsDao.insert(myReview)

    override suspend fun deleteAll() =
        myReviewsDao.deleteAll()
}