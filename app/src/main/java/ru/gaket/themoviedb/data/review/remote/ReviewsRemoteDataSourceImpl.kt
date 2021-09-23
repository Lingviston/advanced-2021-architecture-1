package ru.gaket.themoviedb.data.review.remote

import com.google.firebase.firestore.FirebaseFirestore
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneReview
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.awaitTask
import ru.gaket.themoviedb.util.mapSuccess
import javax.inject.Inject

class ReviewsRemoteDataSourceImpl @Inject constructor() : ReviewsRemoteDataSource {

    private val firebaseFirestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    override suspend fun getMyReviews(userId: User.Id): OperationResult<List<MyReview>, Throwable> =
        firebaseFirestore.collectionGroup(REVIEWS_COLLECTION)
            .whereEqualTo(AUTHOR_ID, userId.value)
            .get()
            .awaitTask()
            .mapSuccess { querySnapshot ->
                querySnapshot.documents
                    .mapNotNull { reviewDoc -> reviewDoc.toMyReview() }
            }

    override suspend fun getMovieReviews(movieId: MovieId): OperationResult<List<SomeoneReview>, Throwable> =
        firebaseFirestore.collection(MOVIES_COLLECTION)
            .document(movieId.toString())
            .collection(REVIEWS_COLLECTION)
            .get()
            .awaitTask()
            .mapSuccess { querySnapshot ->
                querySnapshot.documents
                    .mapNotNull { reviewDoc -> reviewDoc.toSomeoneReview() }
            }

    override suspend fun addReview(
        request: AddReviewRequest,
        authorId: User.Id,
        authorEmail: User.Email,
    ): OperationResult<MyReview, Throwable> =
        firebaseFirestore.collection(MOVIES_COLLECTION)
            .document(request.movieId.toString())
            .collection(REVIEWS_COLLECTION)
            .add(request.toDatastoreMap(authorId, authorEmail))
            .awaitTask()
            .mapSuccess { documentReference -> request.toMyReview(documentReference) }
}