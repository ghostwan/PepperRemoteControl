package com.ghostwan.pepperremote.util

import android.app.Activity
import android.view.View
import android.view.WindowManager

fun Activity.hideSystemBars() {
    if (hasFlags(window.decorView.systemUiVisibility, View.SYSTEM_UI_FLAG_FULLSCREEN, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)) {
        return
    }

    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun hasFlags(visibility: Int, vararg flags: Int): Boolean {
    for (flag in flags) {
        if (visibility and flag != flag) {
            return false
        }
    }
    return true
}