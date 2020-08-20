package com.example.cryptofunding.ui.viewholder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.LoggedWallet
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

    class ViewHolder(private val view: View): FastAdapter.ViewHolder<ProjectItem>(view) {
        private val title: TextView = view.projectTitleTextView
        private val category: TextView = view.projectCategoryTextView
        private val percentFunded: TextView = view.fundedPercentTextView
        val background: ImageView = view.projectImageView
        val likeCardView: CardView = view.favCardView
        private val lottieLikeAnimation: LottieAnimationView = view.projectLikeAnimationView

        override fun bindView(item: ProjectItem, payloads: List<Any>) {
            title.text = item.project.name
            category.text = item.project.category.title
            percentFunded.text = view.resources.getString(R.string.percentFinanced, item.project.percentFunded)

            Glide.with(view).load(item.project.imagesUrl[0]).into(background)

            if (item.project.isFavorite) {
                lottieLikeAnimation.progress = 0.5f
            }

            if (LoggedWallet.currentlyLoggedWallet == null) {
                likeCardView.visibility = View.GONE
            }

            ViewCompat.setTransitionName(likeCardView, item.project.id + "_card")
            ViewCompat.setTransitionName(background, item.project.id + "_image")
            ViewCompat.setTransitionName(category, item.project.id + "_category")
            ViewCompat.setTransitionName(title, item.project.id + "_title")
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