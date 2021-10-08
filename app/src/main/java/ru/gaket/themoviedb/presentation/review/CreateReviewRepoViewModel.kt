package ru.gaket.themoviedb.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.models.CreateReviewForm
import ru.gaket.themoviedb.domain.review.models.CreateReviewState
import ru.gaket.themoviedb.domain.review.models.CreateReviewStep
import ru.gaket.themoviedb.domain.review.models.MovieWithCreateReviewState
import ru.gaket.themoviedb.domain.review.models.Rating
import ru.gaket.themoviedb.domain.review.models.ReviewDraft
import ru.gaket.themoviedb.domain.review.repository.CreateReviewRepository
import ru.gaket.themoviedb.domain.review.store.ItemStore
import ru.gaket.themoviedb.util.Result
import ru.gaket.themoviedb.util.VoidResult
import ru.gaket.themoviedb.util.doOnSuccess
import ru.gaket.themoviedb.util.mapNestedSuccess
import ru.gaket.themoviedb.util.previous
import timber.log.Timber

class CreateReviewRepoViewModel @AssistedInject constructor(
    @Assisted private val movieId: MovieId,
    private val moviesRepository: MoviesRepository,
    private val createReviewFormStore: ItemStore<CreateReviewState>,
) : ViewModel(), CreateReviewRepository {

    val state: LiveData<MovieWithCreateReviewState> =
        observeState()
            .asLiveData(viewModelScope.coroutineContext)

    init {
        Timber.d("CreateReviewRepoViewModel init")

        createReviewFormStore.item = CreateReviewState(
            form = CreateReviewForm.newEmptyModelInstance(movieId),
            step = CreateReviewStep.WHAT_LIKED
        )
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("CreateReviewRepoViewModel onCleared")
    }

    override fun observeState(): Flow<MovieWithCreateReviewState> =
        combine(
            flow {
                val details = moviesRepository.getMovieDetails(movieId)
                if (currentCoroutineContext().isActive) {
                    emit(details)
                }
            },
            observeCreateReviewState()
        ) { movieResult, state ->
            when (movieResult) {
                is Result.Success -> MovieWithCreateReviewState.Data(movieResult.result, state)
                is Result.Error -> MovieWithCreateReviewState.NoMovie
            }
        }

    override fun setWhatLike(whatLiked: String) =
        createReviewFormStore.updateSafely { state ->
            state.copy(
                form = state.form.copy(whatLiked = whatLiked),
                step = CreateReviewStep.WHAT_NOT_LIKED
            )
        }

    override fun setWhatDidNotLike(whatDidNotLike: String) =
        createReviewFormStore.updateSafely { state ->
            state.copy(
                form = state.form.copy(whatDidNotLike = whatDidNotLike),
                step = CreateReviewStep.RATING
            )
        }

    override fun setRating(rating: Rating) =
        createReviewFormStore.updateSafely { state ->
            state.copy(form = state.form.copy(rating = rating))
        }

    override suspend fun submit(
        authorId: User.Id,
        authorEmail: User.Email,
    ): VoidResult<Throwable> =
        buildReview()
            .mapNestedSuccess { draft ->
                moviesRepository.addReview(
                    draft = draft,
                    authorId = authorId,
                    authorEmail = authorEmail
                )
            }
            .doOnSuccess {
                createReviewFormStore.updateSafely { state ->
                    state.copy(step = CreateReviewStep.FINISH)
                }
            }

    private fun observeCreateReviewState(): Flow<CreateReviewState> =
        createReviewFormStore.observeItem()
            .filterNotNull()

    fun previousState() {
        createReviewFormStore.updateSafely { state ->
            val newStep = (state.step.previous() ?: CreateReviewStep.FINISH)
            state.copy(step = newStep)
        }
    }

    private fun buildReview(): Result<ReviewDraft, Throwable> {
        val form = createReviewFormStore.item?.form

        return if ((form != null)
            && (form.whatLiked != null)
            && (form.whatDidNotLike != null)
            && form.rating != null
        ) {
            Result.Success(ReviewDraft(
                movieId = form.movieId,
                liked = form.whatLiked,
                disliked = form.whatDidNotLike,
                rating = form.rating
            ))
        } else {
            Result.Error(IllegalStateException("Form not filled"))
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(movieId: MovieId): CreateReviewRepoViewModel
    }
}