package com.example.cryptofunding

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.CategoryItem
import com.example.cryptofunding.ui.viewholder.ProjectItem
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.select.selectExtension
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_project.*
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private val categoryItemAdapter = ItemAdapter<CategoryItem>()
    private val categoryFastAdapter = FastAdapter.with(categoryItemAdapter)
    private val projectItemAdapter = ItemAdapter<ProjectItem>()
    private val projectFastAdapter = FastAdapter.with(projectItemAdapter)
    private var currentItemPosition: Int? = null

    private val viewModel by viewModel {
        requireActivity().injector.projectsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()

        viewModel.getProjects().observe(viewLifecycleOwner) { projects ->
            setupProjectsList(projects)
            stopLoading()
        }

        setupCategoriesList(viewModel.getCategories())
        handleSeeMoreClickListener()
    }

    private fun setupToolBar() {
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        requireActivity().toolbarTitle.text = getString(R.string.app_name)
    }

    private fun handleSeeMoreClickListener() {
        seeMoreTextView.setOnClickListener {
            (parentFragment as MainFragment).goToSeeDetailedList()
        }
    }

    private fun setupProjectsList(projects: List<Project>) {
        projectsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        projectsRecyclerView.adapter = projectFastAdapter

        projectFastAdapter.addEventHook(object: ClickEventHook<ProjectItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is ProjectItem.ViewHolder) {
                    viewHolder.likeCardView
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ProjectItem>,
                item: ProjectItem
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
        projectItemAdapter.clear()
        projectItemAdapter.add(projects.map {
            ProjectItem(it)
        })
    }

    private fun setupCategoriesList(categories: List<Category>) {
        setCategoryOnClickListener()

        categoryFastAdapter.selectExtension {
            isSelectable = true
            allowDeselection = true
            selectWithItemUpdate = true
        }

        homeCategoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeCategoryRecyclerView.adapter = categoryFastAdapter

        if (categoryItemAdapter.adapterItemCount == 0) {
            categoryItemAdapter.add(categories.map {
                CategoryItem(it)
            })
        }
    }

    private fun setCategoryOnClickListener() {
        categoryFastAdapter.onClickListener = { view, _, item, index ->
            view?.let {
                it.categoryCardView.animate()
                    .setDuration(75)
                    .scaleX(1.08f)
                    .scaleY(1.08f)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction {
                        it.categoryCardView.animate()
                            .setDuration(75)
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setInterpolator(LinearInterpolator())
                            .setListener(null)
                            .start()
                    }
                    .start()
            }
            if (!item.isSelected) {
                viewModel.setCurrentCategory(null)
                item.isSelected = false
                deselectCategory()
                currentItemPosition = null
            }
            else {
                viewModel.setCurrentCategory(item.category)
                item.isSelected = true
                deselectCategory()
                currentItemPosition = index
                selectCategory()
            }
            true
        }
    }

    private fun deselectCategory() {
        currentItemPosition?.let {
            homeCategoryRecyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categoryCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackgroundWhiteApp))
                view.categoryImage.drawable.mutate().setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBackgroundDarkApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun selectCategory() {
        currentItemPosition?.let {
            homeCategoryRecyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categoryCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackgroundDarkApp))
                view.categoryImage.drawable.mutate().setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBackgroundWhiteApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun stopLoading() {
        loadingAnimation.visibility = View.GONE
        projectsRecyclerView.visibility = View.VISIBLE
    }
}
