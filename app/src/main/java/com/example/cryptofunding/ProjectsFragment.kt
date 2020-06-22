package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.adapter.ProjectsAdapter
import com.example.cryptofunding.viewmodel.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.*

/**
 * A simple [Fragment] subclass.
 */
class ProjectsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()

        val viewPagerAdapter = ProjectsAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.funded_projects)
                1 -> tab.text = resources.getString(R.string.my_projects)
            }
        }.attach()
    }

    private fun setupToolBar() {
        activity?.toolbarTitle?.text = getString(R.string.homeBottomProjects)
    }

}
