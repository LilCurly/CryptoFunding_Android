package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.cryptofunding.data.Project
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.LoggedWallet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        if (childFragmentManager.fragments.size == 0) {
            childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.bottomNavMain -> {
                    childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
                    true
                }
                R.id.bottomNavFav -> {
                    childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FavoritesFragment()).commit()
                    true
                }
                R.id.bottomNavProjects -> {
                    childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ProjectsFragment()).commit()
                    true
                }
                else -> {
                    childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
                    true
                }
            }
        }

        if (LoggedWallet.currentlyLoggedWallet == null) {
            bottomNavigation.visibility = View.GONE
        }
    }

    private fun setupToolbar() {
        requireActivity().walletFrameLayout.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToWalletListFragment()
            findNavController().navigate(action)
        }

        requireActivity().addFrameLayout.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }

        requireActivity().walletFrameLayout.visibility = View.VISIBLE
        requireActivity().addFrameLayout.visibility = View.VISIBLE
        requireActivity().closeFrameLayout.visibility = View.GONE
    }

    fun goToSeeDetailedList() {
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, DetailedProjectListFragment()).addToBackStack("test").commit()
    }

    fun getToProjectDetail(project: Project) {
        val action = MainFragmentDirections.actionMainFragmentToProjectDetailFragment(project)
        findNavController().navigate(action)
    }

}
