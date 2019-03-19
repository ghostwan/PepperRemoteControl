package com.ghostwan.pepperremotecontrol

import android.util.Log

private val TAG = "PepperRemote"

fun logInfo(value: String){
    Log.i(TAG, "$value")
}

fun logError(e: Throwable) {
    Log.e(TAG, "exception", e)
}