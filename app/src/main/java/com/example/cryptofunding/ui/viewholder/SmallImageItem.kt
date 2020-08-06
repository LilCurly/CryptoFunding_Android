package com.example.cryptofunding.ui.viewholder

import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.imagepicker.features.ImagePicker
import com.example.cryptofunding.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_small_image.view.*

class SmallImageItem(private val itemType: ItemType,
                     val bitmap: Bitmap? = null,
                     val notifyAddImageClicked: (() -> Unit)? = null,
                     val onLongClickListener: ((index: Int) -> Unit)? = null): AbstractItem<FastAdapter.ViewHolder<SmallImageItem>>() {

    companion object {
        enum class ItemType {
            SMALL_IMAGE,
            ADD_IMAGE
        }
    }

    override val layoutRes: Int
        get() = if (itemType == ItemType.SMALL_IMAGE) R.layout.item_small_image else R.layout.item_add_image
    override val type: Int
        get() = if (itemType == ItemType.SMALL_IMAGE) R.id.itemSmallImage else R.id.itemAddImage

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<SmallImageItem> {
        return if (itemType == ItemType.SMALL_IMAGE) SmallImageViewHolder(v) else AddImageViewHolder(v)
    }

    class SmallImageViewHolder(val view: View): FastAdapter.ViewHolder<SmallImageItem>(view) {
        private var image = view.uploadedImage

        override fun bindView(item: SmallImageItem, payloads: List<Any>) {
            image.setImageBitmap(item.bitmap)
            image.setOnLongClickListener {
                item.onLongClickListener?.let {
                    it(adapterPosition)
                }
                true
            }
        }

        override fun unbindView(item: SmallImageItem) {
            image.setImageBitmap(null)
            image.setOnLongClickListener(null)
        }
    }

    class AddImageViewHolder(val view: View): FastAdapter.ViewHolder<SmallImageItem>(view) {
        override fun bindView(item: SmallImageItem, payloads: List<Any>) {
            view.setOnClickListener {
                item.notifyAddImageClicked?.let {
                    it()
                }
            }
        }

        override fun unbindView(item: SmallImageItem) {
            view.setOnClickListener(null)
        }
    }
}