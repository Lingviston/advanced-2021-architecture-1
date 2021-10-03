package ru.gaket.themoviedb.data.review.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import ru.gaket.themoviedb.data.review.local.MyReviewEntity
import ru.gaket.themoviedb.data.review.local.MyReviewsLocalDataSource
import ru.gaket.themoviedb.data.review.local.toModel
import ru.gaket.themoviedb.data.review.remote.ReviewsRemoteDataSource
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel.Companion.newEmptyModelInstance
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.domain.review.store.ItemStore
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewFormStore: ItemStore<ReviewFormModel>,
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource,
    private val myReviewsLocalDataSource: MyReviewsLocalDataSource,
) : ReviewRepository {

    override suspend fun getSomeoneReviews(movieId: MovieId) =
        reviewsRemoteDataSource.getMovieReviews(movieId)

    override fun getMyReviews(movieId: MovieId): Flow<MyReview> {
        return myReviewsLocalDataSource.getFlowByMovieId(movieId)
            .mapNotNull(List<MyReviewEntity>::firstOrNull)
            .map(MyReviewEntity::toModel)
    }

    override suspend fun setMovieId(movieId: MovieId) {

        reviewFormStore.setItem(newEmptyModelInstance(movieId))
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

    override fun buildReview(): AddReviewRequest {
        val review = reviewFormStore.item ?: error("movieId is not provided")

        return AddReviewRequest(
            movieId = review.movieId,
            liked = review.whatLiked ?: error("whatLiked is not provided"),
            disliked = review.whatDidNotLike ?: error("whatDidNotLike is not provided"),
            rating = review.rating ?: error("rating is not provided")
        )
    }

    override suspend fun clearState() {
        reviewFormStore.reset()
    }
}
