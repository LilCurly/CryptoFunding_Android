package com.example.cryptofunding.ui.viewholder

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category_small.view.*

class CategorySmallItem(val category: Category): AbstractItem<CategorySmallItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.item_category_small
    override val type: Int
        get() = R.id.itemCategorySmallId

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(private val view: View): FastAdapter.ViewHolder<CategorySmallItem>(view) {
        private val image: ImageView = view.categoryImageView

        override fun bindView(item: CategorySmallItem, payloads: List<Any>) {
            image.setImageResource(item.category.img)

            if (item.isSelected) {
                setSelected()
            } else {
                setUnselected()
            }
        }

        override fun unbindView(item: CategorySmallItem) {
            image.setImageDrawable(null)

            if (item.isSelected) {
                setUnselected()
            }
        }

        private fun setSelected() {
            view.categorySmallCardView.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.colorBackgroundDarkApp))
            image.drawable.mutate().setColorFilter(ContextCompat.getColor(view.context, R.color.colorBackgroundWhiteApp), PorterDuff.Mode.SRC_IN)
        }

        private fun setUnselected() {
            view.categorySmallCardView.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.colorBackgroundWhiteApp))
            image.drawable.mutate().setColorFilter(ContextCompat.getColor(view.context, R.color.colorBackgroundDarkApp), PorterDuff.Mode.SRC_IN)
        }
    }
}