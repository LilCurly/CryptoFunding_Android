package com.example.cryptofunding.ui.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.LoggedWallet
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_project_small.view.*

class ProjectSmallItem(val project: Project): AbstractItem<ProjectSmallItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.item_project_small
    override val type: Int
        get() = R.id.itemProjectSmallId

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(private val view: View): FastAdapter.ViewHolder<ProjectSmallItem>(view) {
        val poster = view.projectImageView
        private val favAnimation = view.projectLikeAnimationView
        val favCardView = view.favCardView
        private val category = view.categoryTextView
        private val percent = view.percentDoneTextView
        private val title = view.titleTextView

        override fun bindView(item: ProjectSmallItem, payloads: List<Any>) {
            Glide.with(view).load(item.project.imagesUrl[0]).into(poster)
            category.text = item.project.category.title
            percent.text = view.resources.getString(R.string.percentDone, item.project.percentFunded)
            title.text = item.project.name

            if (item.project.isFavorite) {
                favAnimation.progress = 0.5f
            }

            if (LoggedWallet.currentlyLoggedWallet == null) {
                favCardView.visibility = View.GONE
            }

            ViewCompat.setTransitionName(poster, item.project.id + "_image")
        }

        override fun unbindView(item: ProjectSmallItem) {
            poster.setImageDrawable(null)
            category.text = null
            percent.text = null
            title.text = null

            favAnimation.cancelAnimation()
            favAnimation.removeAllAnimatorListeners()
            favAnimation.removeAllUpdateListeners()
            favAnimation.removeAllLottieOnCompositionLoadedListener()
            favAnimation.setMinAndMaxProgress(0.0f, 1.0f)
            favAnimation.progress = 0.0f
        }

    }
}