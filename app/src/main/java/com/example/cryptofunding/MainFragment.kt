package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()

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
                else -> {
                    childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
                    true
                }
            }
        }
    }

    fun goToSeeDetailedList() {
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, DetailedProjectListFragment()).addToBackStack("test").commit()
    }

}
