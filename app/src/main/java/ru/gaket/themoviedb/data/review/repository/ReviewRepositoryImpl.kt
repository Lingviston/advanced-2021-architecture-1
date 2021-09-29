package ru.gaket.themoviedb.data.review.repository

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.domain.review.store.ItemStore
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.doOnError
import ru.gaket.themoviedb.util.runOperationCatching
import timber.log.Timber
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewFormStore: ItemStore<ReviewFormModel>,
) : ReviewRepository {

    override suspend fun setMovieId(movieId: MovieId) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(movieId = movieId)
        }
    }

    override suspend fun setWhatLike(whatLiked: String) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(whatLiked = whatLiked)
        }
    }

    override suspend fun setWhatDidNotLike(whatDidNotLike: String) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(whatDidNotLike = whatDidNotLike)
        }
    }

    override suspend fun setRating(rating: Rating) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(rating = rating)
        }
    }

    override fun buildReview(): OperationResult<AddReviewRequest, Throwable> {
        return runOperationCatching {
            AddReviewRequest(
                movieId = reviewFormStore.item.movieId ?: error("movieId is not provided"),
                liked = reviewFormStore.item.whatLiked ?: error("whatLiked is not provided"),
                disliked = reviewFormStore.item.whatDidNotLike ?: error("whatDidNotLike is not provided"),
                rating = reviewFormStore.item.rating ?: error("rating is not provided")
            )
        }.doOnError {
            Timber.e(it)
        }
    }

    override suspend fun clearState() {
        reviewFormStore.reset()
    }
}