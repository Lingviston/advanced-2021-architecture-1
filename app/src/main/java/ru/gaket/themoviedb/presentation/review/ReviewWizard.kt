package ru.gaket.themoviedb.presentation.review

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.Rating
import javax.inject.Inject

interface ReviewWizard {

    fun init(movieId: MovieId)
    fun setWhatLike(whatLiked: String)
    fun setWhatDidNotLike(whatDidNotLike: String)
    fun setRating(rating: Rating)
    fun buildReview(): AddReviewRequest
    fun clearState()
}

class ReviewWizardImplementation @Inject constructor() : ReviewWizard {

    private var movie: MovieId? = null
    private var whatLiked: String? = null
    private var whatDidNotLike: String? = null
    private var rating: Rating? = null

    override fun init(movieId: MovieId) {
        clearState()
        this.movie = movieId
    }

    override fun setWhatLike(whatLiked: String) {
        this.whatLiked = whatLiked
    }

    override fun setWhatDidNotLike(whatDidNotLike: String) {
        this.whatDidNotLike = whatDidNotLike
    }

    override fun setRating(rating: Rating) {
        this.rating = rating
    }

    override fun buildReview(): AddReviewRequest {
        return AddReviewRequest(
            movieId = movie ?: error("movieId is not provided"),
            liked = whatLiked ?: error("whatLiked is not provided"),
            disliked = whatDidNotLike ?: error("whatDidNotLike is not provided"),
            rating = rating ?: error("rating is not provided")
        )
    }

    override fun clearState() {
        movie = null
        whatLiked = null
        whatDidNotLike = null
        rating = null
    }
}