package com.example.cryptofunding.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FractionLinearLayoutManager(context: Context?, orientation: Int, private val ratio: Float, reverseLayout: Boolean):
    LinearLayoutManager(context, orientation, reverseLayout) {

    override fun generateDefaultLayoutParams() = scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) = scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(
        c: Context?,
        attrs: AttributeSet?
    ) = scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            when(orientation) {
                HORIZONTAL -> {
                    width = (horizontalSpace * ratio).toInt()
                    height = width
                }
                VERTICAL -> {
                    height = (verticalSpace * ratio).toInt()
                    width = height
                }
            }
        }

    private val horizontalSpace get() = width - paddingStart - paddingEnd
    private val verticalSpace get() = height - paddingTop - paddingBottom
}