package com.example.cryptofunding

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.CategorySmallItem
import com.example.cryptofunding.ui.viewholder.ProjectItem
import com.example.cryptofunding.ui.viewholder.ProjectSmallItem
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.select.selectExtension
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detailed_project_list.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category_small.view.*
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * A simple [Fragment] subclass.
 */
class DetailedProjectListFragment : BaseProjectsFragment() {

    private val categoryItemAdapter = ItemAdapter<CategorySmallItem>()
    private val categoryFastAdapter = FastAdapter.with(categoryItemAdapter)
    private var currentItemPosition: Int? = null

    private val args: DetailedProjectListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_project_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbarTitle.text = resources.getString(R.string.app_name)
        setupCategoriesRecyclerView()
        setupProjectsRecyclerView()
    }

    private fun setupProjectsRecyclerView() {
        projectsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        projectsRecyclerView.adapter = fastAdapter

        viewModel.projects.observe(viewLifecycleOwner) {
            itemAdapter.clear()
            itemAdapter.add(it.map { project ->
                ProjectSmallItem(project)
            })
            stopLoading()
        }

        if (!viewModel.hasProjects()) {
            viewModel.getProjects()
        }
    }

    private fun setupCategoriesRecyclerView() {
        setCategoryOnClickListener()

        categoriesReyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesReyclerView.adapter = categoryFastAdapter

        categoryFastAdapter.selectExtension {
            isSelectable = true
            allowDeselection = true
            selectWithItemUpdate = true
        }

        if (categoryItemAdapter.adapterItemCount == 0) {
            categoryItemAdapter.add(viewModel.getCategories().map {
                val item = CategorySmallItem(it)
                if (args.categoryString == it.type.title) {
                    item.isSelected = true
                }
                item
            })
        }
    }

    private fun setCategoryOnClickListener() {
        categoryFastAdapter.onClickListener = { view, _, item, index ->
            view?.let {
                it.categorySmallCardView.animate()
                    .setDuration(75)
                    .scaleX(1.08f)
                    .scaleY(1.08f)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction {
                        it.categorySmallCardView.animate()
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
                viewModel.getProjects()
                item.isSelected = false
                deselectCategory()
                currentItemPosition = null
            }
            else {
                viewModel.setCurrentCategory(item.category)
                viewModel.getProjectsForCategory(item.category.type.title)
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
            categoriesReyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categorySmallCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackgroundWhiteApp))
                view.categoryImageView.drawable.mutate().setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBackgroundDarkApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun selectCategory() {
        currentItemPosition?.let {
            categoriesReyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categorySmallCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackgroundDarkApp))
                view.categoryImageView.drawable.mutate().setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBackgroundWhiteApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun stopLoading() {
        loadingAnimation.visibility = View.GONE
        projectsRecyclerView.visibility = View.VISIBLE
    }
}
