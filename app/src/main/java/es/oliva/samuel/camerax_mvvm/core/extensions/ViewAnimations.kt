package es.oliva.samuel.camerax_mvvm.core.extensions

import android.animation.Animator
import android.view.View
import androidx.core.view.isVisible

fun View.animatedScale(scaleX: Float, scaleY: Float, milliseconds: Long) {
    animate().scaleX(scaleX).scaleY(scaleY).setDuration(milliseconds)
}

fun View.animatedFadeIn(milliseconds: Long) {
    isVisible = true
    if (alpha < 1f) animate().alpha(1f).setDuration(milliseconds)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(p0: Animator) {
                alpha = 1f
            }

            override fun onAnimationEnd(p0: Animator) {}
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
}

fun View.animatedFadeOut(milliseconds: Long) {
    if (alpha > 0f) animate().alpha(0f).setDuration(milliseconds)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                isVisible = false
            }

            override fun onAnimationCancel(p0: Animator) {
                isVisible = false; alpha = 0f
            }

            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
}
