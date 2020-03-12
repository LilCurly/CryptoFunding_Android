package com.example.cryptofunding.utils

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun Activity.getRootView(): View {
    return findViewById<View>(android.R.id.content)
}
fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}
fun Fragment.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.activity?.let {
        it.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = it.getRootView().height - visibleBounds.height()
        val marginOfError = it.convertDpToPx(50F).roundToInt()
        return heightDiff > marginOfError
    }
    return false
}

fun Fragment.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}

inline fun getValueAnimator(
    forward: Boolean = true,
    duration: Long,
    interpolator: TimeInterpolator,
    crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a =
        if (forward) ValueAnimator.ofFloat(0f, 1f)
        else ValueAnimator.ofFloat(1f, 0f)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}