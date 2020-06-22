package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.ProjectSmallItem
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_my_projects.*

/**
 * A simple [Fragment] subclass.
 */
class MyProjectsFragment : Fragment() {
    private val itemAdapter = ItemAdapter<ProjectSmallItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    private val viewModel by viewModel {
        activity!!.injector.projectsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = fastAdapter

        itemAdapter.add(viewModel.getProjects().map {
            ProjectSmallItem(it)
        })
    }

}
