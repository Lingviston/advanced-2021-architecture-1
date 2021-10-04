package ru.gaket.themoviedb.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

//https://betterprogramming.pub/how-to-use-kotlin-coroutines-with-firebase-6f8577a3e00f
suspend fun <T> Task<T>.awaitTask(): Result<T, Throwable> {
    if (this.isComplete) {
        val e = this.exception
        return if (e == null) {
            if (this.isCanceled) {
                throw CancellationException("Task $this was cancelled normally.")
            } else {
                Result.Success(requireNotNull(this.result))
            }
        } else {
            Result.Error(e)
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = this.exception
            if (e == null) {
                if (this.isCanceled) {
                    cont.cancel()
                } else {
                    cont.resume(Result.Success(requireNotNull(this.result)))
                }
            } else {
                cont.resume(Result.Error(e))
            }
        }
    }
}
