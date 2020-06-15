package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.data.repository.ProjectRepository
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.ProjectSmallItem
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {
    private val viewModel by viewModel {
        activity!!.injector.projectsViewModel
    }

    private val itemAdapter = ItemAdapter<ProjectSmallItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        projectsRecyclerView.adapter = fastAdapter

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
                if (viewModel.isFavorite(position)) {
                    v.projectLikeAnimationView.setMinAndMaxProgress(0.5f, 1.0f)
                } else {
                    v.projectLikeAnimationView.setMinAndMaxProgress(0.0f, 0.5f)
                }
                v.projectLikeAnimationView.playAnimation()
                viewModel.toggleFavorite(position)
            }

        })

        itemAdapter.add(ProjectRepository.projects.map {
            ProjectSmallItem(it)
        })
    }

}
