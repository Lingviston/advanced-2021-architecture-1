package ru.gaket.themoviedb.domain

import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.data.movies.getMovieDetailsList
import ru.gaket.themoviedb.data.review.remote.ReviewsRemoteDataSource
import ru.gaket.themoviedb.domain.auth.SyncLocalStorageUseCase
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.util.OperationResult
import timber.log.Timber
import javax.inject.Inject

class SyncLocalStorageUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource//todo: replace with MoviesRepo
) : SyncLocalStorageUseCase {

    override suspend fun fill() {
        when (val myReviewsResult = reviewsRemoteDataSource.getAllMyReviews(authRepository.currentUser!!.id)) {
            is OperationResult.Success -> {
                storeMyReviewsWithMovies(myReviewsResult.result)
            }
            is OperationResult.Error   -> {
                Timber.e("fill Local storage error", myReviewsResult.result)
            }
        }
    }

    private suspend fun storeMyReviewsWithMovies(result: List<MyReview>) {
        val myReviewedMovieIds = result
            .map { review -> review.movieId }
            .toSet()

        val movies = moviesRepository.getMovieDetailsList(ids = myReviewedMovieIds)

        Timber.d("fill Local storage", movies)
        //todo 2: save in local storage myReviewedMovies list
        //todo 3: save in local storage myReviewsResult.result
    }

    override suspend fun clear() {
        //todo: clear from local storage my myReviewedMovies
    }
}