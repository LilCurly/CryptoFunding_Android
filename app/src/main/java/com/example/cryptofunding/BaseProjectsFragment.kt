package com.example.cryptofunding

import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.ProjectSmallItem
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.item_project.view.*
import kotlinx.android.synthetic.main.item_project.view.projectImageView
import kotlinx.android.synthetic.main.item_project.view.projectLikeAnimationView
import kotlinx.android.synthetic.main.item_project_small.view.*

open class BaseProjectsFragment: Fragment() {
    val viewModel by viewModel {
        requireActivity().injector.projectsViewModel
    }

    val itemAdapter = ItemAdapter<ProjectSmallItem>()
    val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fastAdapter.addEventHook(object: ClickEventHook<ProjectSmallItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is ProjectSmallItem.ViewHolder) {
                    viewHolder.favCardView
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ProjectSmallItem>,
                item: ProjectSmallItem
            ) {
                item.project.id?.let {
                    if (viewModel.isFavorite(position)) {
                        viewModel.removeFavorite(it)
                        v.projectLikeAnimationView.setMinAndMaxProgress(0.5f, 1.0f)
                    } else {
                        viewModel.setFavorite(it)
                        v.projectLikeAnimationView.setMinAndMaxProgress(0.0f, 0.5f)
                    }
                    v.projectLikeAnimationView.playAnimation()
                    viewModel.toggleFavorite(position)
                }
            }
        })

        fastAdapter.onClickListener = { v, _, item, _ ->
            val b = parentFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val b1 = b is DetailedProjectListFragment
            if (b1) {
                val action = DetailedProjectListFragmentDirections.actionDetailedProjectListFragmentToProjectDetailFragment(item.project, item.getViewHolder(v!!).poster.drawable.toBitmap())
                val extras = FragmentNavigatorExtras(v!!.projectImageView to item.project.id + "_image")
                findNavController().navigate(action, extras)
            }
            else {
                (parentFragment as MainFragment).getToProjectDetail(item.project, v!!.projectImageView, item.getViewHolder(v).poster.drawable.toBitmap())
            }
            true
        }
    }
}