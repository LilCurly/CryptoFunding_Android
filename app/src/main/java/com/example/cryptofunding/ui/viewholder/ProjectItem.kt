package com.example.cryptofunding.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Project
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_project.view.*

class ProjectItem(val project: Project): AbstractItem<ProjectItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_project
    override val type: Int
        get() = R.id.itemProject

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View): FastAdapter.ViewHolder<ProjectItem>(view) {
        private val title: TextView = view.projectTitleTextView
        private val category: TextView = view.projectCategoryTextView
        private val percentFunded: TextView = view.fundedPercentTextView
        private val background: ImageView = view.projectImageView

        override fun bindView(item: ProjectItem, payloads: MutableList<Any>) {
            title.text = item.project.name
            category.text = item.project.category.title
            percentFunded.text = "${item.project.percentFunded}% financé"

            background.setImageResource(item.project.img)
        }

        override fun unbindView(item: ProjectItem) {
            title.text = null
            category.text = null
            percentFunded.text = null
            background.setImageDrawable(null)
        }

    }
}