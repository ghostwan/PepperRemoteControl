package com.ghostwan.pepperremote.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun ImageView.start(infinite: Boolean, onStart: (() -> Unit)?=null): AnimatedVectorDrawableCompat {
    val animDrawable = this.drawable as AnimatedVectorDrawableCompat
    if (infinite) {
        animDrawable.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationStart(drawable: Drawable?) {
                onStart?.invoke()
            }

            override fun onAnimationEnd(drawable: Drawable?) {
                this@start.post {
                    animDrawable.start()
                }
            }
        })
    }
    animDrawable.start()
    return animDrawable
}