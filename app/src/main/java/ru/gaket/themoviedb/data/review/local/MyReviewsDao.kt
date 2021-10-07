package ru.gaket.themoviedb.data.review.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.movies.models.MovieId

@Dao
interface MyReviewsDao {

    @Query("""
        SELECT *
        FROM ${MyReviewEntity.TABLE_NAME}
        WHERE ${MyReviewEntity.REVIEW_MOVIE_ID}=:movieId
        LIMIT 1
        """)
    suspend fun getByMovieId(movieId: MovieId): MyReviewEntity?

    @Query("""
        SELECT *
        FROM ${MyReviewEntity.TABLE_NAME}
        WHERE ${MyReviewEntity.REVIEW_MOVIE_ID}=:movieId
        LIMIT 1
        """)
    fun observeMoviesById(movieId: MovieId): Flow<List<MyReviewEntity>>

    @Query("""
        SELECT *
        FROM ${MyReviewEntity.TABLE_NAME}
        WHERE ${MyReviewEntity.REVIEW_MOVIE_ID} IN (:movieIds)
        """)
    suspend fun getByMovieIds(movieIds: List<MovieId>): List<MyReviewEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myReviews: List<MyReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myReview: MyReviewEntity)

    @Transaction
    @Query("DELETE FROM ${MyReviewEntity.TABLE_NAME}")
    suspend fun deleteAll()
}