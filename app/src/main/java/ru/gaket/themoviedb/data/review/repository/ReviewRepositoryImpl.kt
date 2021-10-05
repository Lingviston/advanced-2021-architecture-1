package ru.gaket.themoviedb.data.review.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.models.ReviewDraft
import ru.gaket.themoviedb.domain.review.models.Rating
import ru.gaket.themoviedb.domain.review.models.ReviewFormModel
import ru.gaket.themoviedb.domain.review.models.ReviewFormModel.Companion.newEmptyModelInstance
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.domain.review.store.ItemStore
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewFormStore: ItemStore<ReviewFormModel>,
) : ReviewRepository {

    override val reviewState: Flow<ReviewFormModel>
        get() = reviewFormStore.itemChanges.filterNotNull()

    override suspend fun setMovieId(movieId: MovieId) {

        reviewFormStore.setItem(newEmptyModelInstance(movieId))
    }

    override suspend fun setWhatLike(whatLiked: String?) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(whatLiked = whatLiked)
        }
    }

    override suspend fun setWhatDidNotLike(whatDidNotLike: String?) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(whatDidNotLike = whatDidNotLike)
        }
    }

    override suspend fun setRating(rating: Rating?) {
        reviewFormStore.updateItem { reviewForm ->
            reviewForm.copy(rating = rating)
        }
    }

    override fun buildReview(): ReviewDraft {
        val review = reviewFormStore.item ?: error("movieId is not provided")

        return ReviewDraft(
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