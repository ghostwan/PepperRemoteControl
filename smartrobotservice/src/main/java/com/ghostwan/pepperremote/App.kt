package com.ghostwan.pepperremote

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

    companion object {
        const val EXTRA_ROBOT_ENDPOINT = "EXTRA_ROBOT_ENDPOINT"
        const val EXTRA_PUBLIC_TOKEN = "EXTRA_PUBLIC_TOKEN"
        const val EXTRA_FOCUS_TOKEN = "EXTRA_FOCUS_TOKEN"
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant()
        }
        Timber.tag("PepperRemote")
    }
}



