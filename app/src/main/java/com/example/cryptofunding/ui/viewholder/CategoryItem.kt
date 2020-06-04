package com.example.cryptofunding.ui.viewholder

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Category
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryItem(val category: Category): AbstractItem<CategoryItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.item_category
    override val type: Int
        get() = R.id.categoryName

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(private val view: View): FastAdapter.ViewHolder<CategoryItem>(view) {
        private val title: TextView = view.categoryName
        private val image: ImageView = view.categoryImage

        override fun bindView(item: CategoryItem, payloads: MutableList<Any>) {
            title.text = item.category.type.title
            image.setImageResource(item.category.img)
            if (item.isSelected) {
                setSelected()
            } else {
                setUnselected()
            }
        }

        override fun unbindView(item: CategoryItem) {
            title.text = null
            image.setImageDrawable(null)
            if (item.isSelected) {
                setUnselected()
            }
        }

        private fun setSelected() {
            view.categoryCardView.setCardBackgroundColor(view.resources.getColor(R.color.colorBackgroundDarkApp))
            image.drawable.mutate().setColorFilter(view.resources.getColor(R.color.colorBackgroundWhiteApp), PorterDuff.Mode.SRC_IN)
        }

        private fun setUnselected() {
            view.categoryCardView.setCardBackgroundColor(view.resources.getColor(R.color.colorBackgroundWhiteApp))
            image.drawable.mutate().setColorFilter(view.resources.getColor(R.color.colorBackgroundDarkApp), PorterDuff.Mode.SRC_IN)
        }
    }
}