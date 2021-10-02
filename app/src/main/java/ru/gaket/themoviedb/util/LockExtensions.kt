package ru.gaket.themoviedb.util

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract

@ExperimentalContracts
inline fun <T> synchronizedRead(lock: ReentrantReadWriteLock, action: () -> T): T {
    contract {
        callsInPlace(action, EXACTLY_ONCE)
    }

    return synchronizedAccess(lock.readLock(), action)
}

@ExperimentalContracts
inline fun <T> synchronizedWrite(lock: ReentrantReadWriteLock, action: () -> T): T {
    contract {
        callsInPlace(action, EXACTLY_ONCE)
    }

    return synchronizedAccess(lock.writeLock(), action)
}

@ExperimentalContracts
inline fun <T> synchronizedAccess(lock: Lock, action: () -> T): T {
    contract {
        callsInPlace(action, EXACTLY_ONCE)
    }

    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}
