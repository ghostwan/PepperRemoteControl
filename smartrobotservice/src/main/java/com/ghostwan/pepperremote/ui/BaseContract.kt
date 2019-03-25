package com.ghostwan.pepperremote.ui

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface BaseContract {
    interface View
    interface Presenter<T : View> {
        @CallSuper
        fun takeView(view: T) {
            this.view = view
        }

        @CallSuper
        fun releaseView() {
            this.view = null
        }

        var view: T?
    }
}

interface CoroutineContract {
    interface View : BaseContract.View, CoroutineScope {
        private val job: Job
            get() = Job()
        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.Main

        suspend fun <T> ui(block: suspend CoroutineScope.() -> T): T {
            return withContext(coroutineContext, block)
        }
    }
    interface Presenter<T: View> : BaseContract.Presenter<T>, CoroutineScope {
        private val job: Job
            get() = Job()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO

        override fun releaseView() {
            job.cancel()
            super.releaseView()
        }
    }
}