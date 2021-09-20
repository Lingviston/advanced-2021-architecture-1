package ru.gaket.themoviedb.util

sealed class OperationResult<out S : Any?, out E : Any?> {

    data class Success<out S : Any?>(val result: S) : OperationResult<S, Nothing>()

    data class Error<out E : Any?>(val result: E) : OperationResult<Nothing, E>()
}

typealias VoidOperationResult<E> = OperationResult<Unit, E>

inline fun <S : Any?> OperationResult<S, Throwable>.getOrThrow(): S =
    when (this) {
        is OperationResult.Success -> this.result
        is OperationResult.Error   -> throw this.result
    }

inline fun <S : Any?, E : Any?, R : Any?> OperationResult<S, E>.mapSuccess(block: (S) -> R): OperationResult<R, E> =
    when (this) {
        is OperationResult.Success -> OperationResult.Success(result = block(this.result))
        is OperationResult.Error   -> OperationResult.Error(result = this.result)
    }

inline fun <S : Any?, E : Any?, R : Any?> OperationResult<S, E>.mapError(block: (E) -> R): OperationResult<S, R> =
    when (this) {
        is OperationResult.Success -> OperationResult.Success(result = this.result)
        is OperationResult.Error   -> OperationResult.Error(result = block(this.result))
    }

inline fun <S : Any?, E : Any?, R : Any?> OperationResult<S, E>.mapNestedSuccess(
    block: (S) -> OperationResult<R, E>
): OperationResult<R, E> =
    when (this) {
        is OperationResult.Success -> block(this.result)
        is OperationResult.Error   -> OperationResult.Error(result = this.result)
    }

inline fun <S : Any?, E : Any?> OperationResult<S, E>.doOnSuccess(block: (S) -> Unit): OperationResult<S, E> {
    if (this is OperationResult.Success) {
        block(this.result)
    }
    return this
}

inline fun <S : Any?, E : Any?> OperationResult<S, E>.doOnError(block: (E) -> Unit): OperationResult<S, E> {
    if (this is OperationResult.Error) {
        block(this.result)
    }
    return this
}