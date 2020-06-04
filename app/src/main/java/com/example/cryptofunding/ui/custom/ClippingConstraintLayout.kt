package com.example.cryptofunding.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cryptofunding.R

class ClippingConstraintLayout(mContext: Context, attrSet: AttributeSet): ConstraintLayout(mContext, attrSet) {
    private val clipPath = Path()
    private val rectF = RectF()

    private var topLeftCornerRadius: Float = 0f
    private var topRightCornerRadius: Float = 0f
    private var bottomLeftCornerRadius: Float = 0f
    private var bottomRightCornerRadius: Float = 0f

    private var cornerDimensions: FloatArray

    init {
        val typedArray = mContext.obtainStyledAttributes(attrSet, R.styleable.ClippingConstraintLayout, 0, 0)

        topLeftCornerRadius = typedArray.getDimension(R.styleable.ClippingConstraintLayout_topLeftCornerRadius, 0f)
        topRightCornerRadius = typedArray.getDimension(R.styleable.ClippingConstraintLayout_topRightCornerRadius, 0f)
        bottomLeftCornerRadius = typedArray.getDimension(R.styleable.ClippingConstraintLayout_bottomLeftCornerRadius, 0f)
        bottomRightCornerRadius = typedArray.getDimension(R.styleable.ClippingConstraintLayout_bottomRightCornerRadius, 0f)

        typedArray.recycle()

        cornerDimensions = arrayOf(
            topLeftCornerRadius, topLeftCornerRadius,
            topRightCornerRadius, topRightCornerRadius,
            bottomRightCornerRadius, bottomRightCornerRadius,
            bottomLeftCornerRadius, bottomLeftCornerRadius).toFloatArray()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        clipPath.addRoundRect(rectF, cornerDimensions, Path.Direction.CW)
        canvas?.clipPath(clipPath)
        super.onDraw(canvas)
    }
}