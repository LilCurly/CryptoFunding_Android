package com.example.cryptofunding.ui.viewholder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.DEBUG
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
        val likeCardView: CardView = view.favCardView
        val lottieLikeAnimation: LottieAnimationView = view.projectLikeAnimationView

        override fun bindView(item: ProjectItem, payloads: List<Any>) {
            title.text = item.project.name
            category.text = item.project.category.title
            percentFunded.text = "${item.project.percentFunded}% financé"

            background.setImageResource(item.project.img)
            if (item.project.isFavorite) {
                lottieLikeAnimation.progress = 0.5f
            }
        }

        override fun unbindView(item: ProjectItem) {
            title.text = null
            category.text = null
            percentFunded.text = null
            background.setImageDrawable(null)

            lottieLikeAnimation.cancelAnimation()
            lottieLikeAnimation.removeAllAnimatorListeners()
            lottieLikeAnimation.removeAllUpdateListeners()
            lottieLikeAnimation.removeAllLottieOnCompositionLoadedListener()
            lottieLikeAnimation.setMinAndMaxProgress(0.0f, 1.0f)
            lottieLikeAnimation.progress = 0.0f
        }

    }
}