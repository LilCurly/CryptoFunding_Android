package com.example.cryptofunding.utils

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
    this.activity!!.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = this.activity!!.getRootView().height - visibleBounds.height()
    val marginOfError = this.activity!!.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}

fun Fragment.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}