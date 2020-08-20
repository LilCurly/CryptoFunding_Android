package com.example.cryptofunding.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cryptofunding.R
import com.example.cryptofunding.data.SliderImage
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.slider_image.view.*

class SliderAdapter(private val context: Context, private val items: List<SliderImage>): SliderViewAdapter<SliderAdapter.ViewHolder>() {
    var prevIndex = 0
    var alreadySet = false

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.slider_image, null)
        return ViewHolder(inflate)
    }

    override fun getCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.let {
            if ((items.count() > 2 && position == items.count() - 1 && prevIndex != items.count() - 2)) {
                return
            }
            if (items.count() == 2 && alreadySet) {
                return
            }
            if (items.count() == 2 && position == 1) {
                alreadySet = true
            }
            prevIndex = position
            if (items[position].bitmap != null) {
                viewHolder.imageView.setImageBitmap(items[position].bitmap)
            }
            else {
                Glide.with(viewHolder.view).load(items[position].url).into(viewHolder.imageView)
            }
        }
    }

    class ViewHolder(val view: View): SliderViewAdapter.ViewHolder(view) {
        val imageView: ImageView = view.sliderImageView
    }
}