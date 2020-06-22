package com.example.cryptofunding.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptofunding.FundedProjectsFragment
import com.example.cryptofunding.MyProjectsFragment

class ProjectsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FundedProjectsFragment()
            1 -> MyProjectsFragment()
            else -> FundedProjectsFragment()
        }
    }
}