package com.example.cryptofunding.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.example.cryptofunding.R

class ProgressBar(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    private var percent: Int = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressBar, 0, 0).apply {
            try {
                percent = getInt(R.styleable.ProgressBar_percent, 0)
            } finally {
                recycle()
            }
        }

        this.background = ContextCompat.getDrawable(context, R.drawable.background_progressbar)

        viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                val view = View(context)

                val viewWidth = (width * percent) / 100

                view.layoutParams = LayoutParams(viewWidth, LayoutParams.MATCH_PARENT)
                view.background = ContextCompat.getDrawable(context, R.drawable.background_progressbar_second)

                addView(view)
            }

        })
    }
}