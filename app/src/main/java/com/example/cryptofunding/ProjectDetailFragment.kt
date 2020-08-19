package com.example.cryptofunding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.TaskAdapter
import com.example.cryptofunding.utils.DEBUG
import kotlinx.android.synthetic.main.fragment_project_detail.*
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class ProjectDetailFragment : Fragment() {

    private val viewModel by viewModel {
        requireActivity().injector.projectDetailViewModel
    }

    private val imageList by lazy {
        mutableListOf<SlideModel>()
    }

    private val args: ProjectDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()

        val project = args.project

        project.imagesUrl.forEach {
            imageList.add(SlideModel(it))
        }
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        viewModel.tasks.observe(viewLifecycleOwner) {
            setupTasks(it.toMutableList())
        }
        viewModel.loadTasksForId(project.id!!)

        motionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                imageSlider.isTouchable = false
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentState: Int) {
                val end = R.id.end

                imageSlider.isTouchable = currentState != end
            }

        })
    }

    private fun hideToolbar() {
        requireActivity().toolbar.visibility = View.GONE
    }

    private fun setupTasks(listOfTasks: MutableList<Task>) {
        tasksRecyclerView.adapter =
            TaskAdapter(requireContext(), listOfTasks)
        tasksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


}
