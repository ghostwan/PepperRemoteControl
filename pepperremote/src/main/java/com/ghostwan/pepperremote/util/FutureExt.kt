package com.ghostwan.pepperremote.util

import com.aldebaran.qi.Future
import java.util.concurrent.CancellationException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by erwan on 10/03/2018.
 */
suspend fun <T> Future<T>.await(): T =
        suspendCoroutine { cont: Continuation<T> ->
            this.thenConsume {
                when {
                    it.isSuccess -> cont.resume(it.value)
                    it.isCancelled -> cont.resumeWithException(CancellationException())
                    else -> cont.resumeWithException(it.error)
                }
            }
        }