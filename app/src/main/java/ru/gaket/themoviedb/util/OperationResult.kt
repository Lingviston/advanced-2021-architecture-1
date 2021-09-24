package ru.gaket.themoviedb.util

sealed class OperationResult<out S, out E> {

    data class Success<out S>(val result: S) : OperationResult<S, Nothing>()

    data class Error<out E>(val result: E) : OperationResult<Nothing, E>()
}

typealias VoidOperationResult<E> = OperationResult<Unit, E>

@UnitTestable
inline fun <S> OperationResult<S, Throwable>.getOrThrow(): S =
    when (this) {
        is OperationResult.Success -> this.result
        is OperationResult.Error -> throw this.result
    }

@UnitTestable
inline fun <S, E, R> OperationResult<S, E>.mapSuccess(block: (S) -> R): OperationResult<R, E> =
    when (this) {
        is OperationResult.Success -> OperationResult.Success(result = block(this.result))
        is OperationResult.Error -> OperationResult.Error(result = this.result)
    }

@UnitTestable
inline fun <S, E, R> OperationResult<S, E>.mapError(block: (E) -> R): OperationResult<S, R> =
    when (this) {
        is OperationResult.Success -> OperationResult.Success(result = this.result)
        is OperationResult.Error -> OperationResult.Error(result = block(this.result))
    }

@UnitTestable
inline fun <S, E, R> OperationResult<S, E>.mapNestedSuccess(
    block: (S) -> OperationResult<R, E>,
): OperationResult<R, E> =
    when (this) {
        is OperationResult.Success -> block(this.result)
        is OperationResult.Error -> OperationResult.Error(result = this.result)
    }

@UnitTestable
inline fun <S, E> OperationResult<S, E>.doOnSuccess(block: (S) -> Unit): OperationResult<S, E> {
    if (this is OperationResult.Success) {
        block(this.result)
    }
    return this
}

@UnitTestable
inline fun <S, E> OperationResult<S, E>.doOnError(block: (E) -> Unit): OperationResult<S, E> {
    if (this is OperationResult.Error) {
        block(this.result)
    }
    return this
}

@UnitTestable
inline fun <S, R> S.runOperationCatching(block: S.() -> R): OperationResult<R, Throwable> =
    try {
        OperationResult.Success(block())
    } catch (e: Throwable) {
        OperationResult.Error(e)
    }

@UnitTestable
inline fun <reified S, reified E> List<OperationResult<S, E>>.toSuccessOrErrorList(): OperationResult<List<S>, List<E>> {
    var successResults: MutableList<S>? = null
    var errorResults: MutableList<E>? = null

    var hasErrors = false

    for (item: OperationResult<S, E> in this) {
        when {
            ((item is OperationResult.Success) && !hasErrors) -> {
                if (successResults == null) {
                    successResults = mutableListOf()
                }

                successResults.add(item.result)
            }
            (item is OperationResult.Error) -> {
                hasErrors = true

                if (errorResults == null) {
                    errorResults = mutableListOf()
                }

                errorResults.add(item.result)
            }
        }
    }

    return if (errorResults != null) {
        OperationResult.Error(result = errorResults)
    } else {
        OperationResult.Success(result = successResults.orEmpty())
    }
}

