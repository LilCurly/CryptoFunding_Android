package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.databinding.FragmentAddTasksBinding
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.custom.AddTaskBottomSheet
import com.example.cryptofunding.ui.viewholder.NewTaskAdapter
import com.example.cryptofunding.utils.DEBUG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_tasks.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.util.*
import kotlin.concurrent.schedule

class AddTasksFragment : Fragment() {
    val viewModel by lazy {
        requireActivity().injector.handleTasksViewModel
    }

    val args: AddTasksFragmentArgs by navArgs()

    private lateinit var tasksAdapter: NewTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddTasksBinding>(inflater, R.layout.fragment_add_tasks, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        viewModel.project = args.project

        tasksAdapter = NewTaskAdapter(requireContext(),
            viewModel.tasks,
            onAddNewTaskClicked = {
                val bottomSheet = AddTaskBottomSheet({
                    viewModel.tasks.add(it)
                    tasksAdapter.notifyItemInserted(viewModel.tasks.size)
                    viewModel.updateCanProceed()
                })
                bottomSheet.show(parentFragmentManager, "TAG")
            }, onRemoveTaskClicked = { position ->
                viewModel.tasks.removeAt(position)
                viewModel.updateCanProceed()
            }, onEditTaskClicked = { position ->
                val bottomSheet = AddTaskBottomSheet({
                    viewModel.tasks[position] = it
                    tasksAdapter.notifyItemChanged(position)
                }, viewModel.tasks[position])
                bottomSheet.show(parentFragmentManager, "TAG")
            })
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        buttonLaunchProject.setOnClickListener {
            viewModel.saveProject(onSuccess = {
                loadingAnimation.visibility = View.GONE
                successAnimation.visibility = View.VISIBLE
                successAnimation.playAnimation()
                Timer("Success", false).schedule(1000) {
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }, onFailure = {
                loadingAnimation.visibility = View.GONE
                failureAnimation.visibility = View.VISIBLE
                failureAnimation.playAnimation()
            }, onWaiting = {
                buttonLaunchProject.isEnabled = false
                buttonLaunchProject.text = ""
                loadingAnimation.visibility = View.VISIBLE
                loadingAnimation.playAnimation()
            })
        }
    }

    private fun setupToolbar() {
        requireActivity().toolbarTitle.text = getString(R.string.create_tasks)
        requireActivity().walletFrameLayout.visibility = View.GONE
        requireActivity().addFrameLayout.visibility = View.GONE
        requireActivity().closeFrameLayout.visibility = View.VISIBLE
        requireActivity().closeFrameLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_toolbar_icon_light)
        requireActivity().closeFrameLayout.setOnClickListener {
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }
}