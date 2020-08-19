package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.data.repository.ProjectRepository
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.ProjectSmallItem
import com.example.cryptofunding.utils.LoggedWallet
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : BaseProjectsFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()

        projectsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        projectsRecyclerView.adapter = fastAdapter

        if (LoggedWallet.currentlyLoggedWallet == null) {
            stopLoadingWithoutWallet()
        }

        viewModel.getFavoritesProjects().observe(viewLifecycleOwner) {
            itemAdapter.clear()
            itemAdapter.add(it.map { project ->
                ProjectSmallItem(project)
            })
            stopLoading()
        }

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
                    if (itemAdapter.adapterItems[position].project.isFavorite) {
                        viewModel.removeFavorite(it)
                        itemAdapter.remove(position)
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

    }

    private fun setupToolBar() {
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorSecondaryApp))
        requireActivity().toolbarTitle.text = getString(R.string.homeBottomFav)
    }

    private fun stopLoading() {
        loadingAnimation.visibility = View.GONE
        projectsRecyclerView.visibility = View.VISIBLE
    }

    private fun stopLoadingWithoutWallet() {
        loadingAnimation.visibility = View.GONE
        noWalletTextView.visibility = View.VISIBLE
    }
}
