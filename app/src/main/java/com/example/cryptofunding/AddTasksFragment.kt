package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.ui.custom.AddTaskBottomSheet
import com.example.cryptofunding.ui.viewholder.NewTaskAdapter
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.NewProjectViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_add_tasks.*

class AddTasksFragment : Fragment() {
    private lateinit var tasksAdapter: RecyclerSwipeAdapter<RecyclerView.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val task = Task("Test", "Ok coll", 10, "12/08/20")
        tasksAdapter = NewTaskAdapter(requireContext(),
            mutableListOf(task, task, task, task, task, task, task),
            {
                val bottomSheet = AddTaskBottomSheet()
                bottomSheet.show(parentFragmentManager, "TAG")
        })
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}