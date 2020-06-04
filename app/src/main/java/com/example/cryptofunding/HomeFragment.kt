package com.example.cryptofunding

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptofunding.data.Category
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.CategoryItem
import com.example.cryptofunding.ui.viewholder.ProjectItem
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.selectExtension
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_category.view.*

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
        activity!!.injector.homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = viewModel.categories
        val projects = viewModel.projects

        setupCategoriesList(categories)
        setupProjectsList(projects)
    }

    private fun setupProjectsList(projects: List<Project>) {
        projectsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        projectsRecyclerView.adapter = projectFastAdapter

        projectItemAdapter.add(projects.map {
            ProjectItem(it)
        })
    }

    private fun setupCategoriesList(categories: List<Category>) {
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

        categoryFastAdapter.selectExtension {
            isSelectable = true
            allowDeselection = true
            selectWithItemUpdate = true
        }

        homeCategoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeCategoryRecyclerView.adapter = categoryFastAdapter

        categoryItemAdapter.add(categories.map {
            CategoryItem(it)
        })
    }

    private fun deselectCategory() {
        currentItemPosition?.let {
            homeCategoryRecyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categoryCardView.setCardBackgroundColor(resources.getColor(R.color.colorBackgroundWhiteApp))
                view.categoryImage.drawable.mutate().setColorFilter(resources.getColor(R.color.colorBackgroundDarkApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun selectCategory() {
        currentItemPosition?.let {
            homeCategoryRecyclerView.layoutManager?.findViewByPosition(it)?.let { view ->
                view.categoryCardView.setCardBackgroundColor(resources.getColor(R.color.colorBackgroundDarkApp))
                view.categoryImage.drawable.mutate().setColorFilter(resources.getColor(R.color.colorBackgroundWhiteApp), PorterDuff.Mode.SRC_IN)
            }
        }
    }
}
