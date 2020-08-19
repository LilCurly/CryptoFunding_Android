package com.example.cryptofunding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.TaskAdapter
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.LoggedWallet
import kotlinx.android.synthetic.main.fragment_project_detail.*
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

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

        viewModel.project = args.project

        viewModel.project.imagesUrl.forEach {
            imageList.add(SlideModel(it))
        }
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        viewModel.tasks.observe(viewLifecycleOwner) {
            setupTasks(it.toMutableList())
            calcultateTotalAmount(it)
            stopLoading()
        }
        viewModel.loadTasksForId(viewModel.project.id!!)

        textType.text = viewModel.project.category.title
        textTitle.text = viewModel.project.name
        textSummary.text = viewModel.project.summary

        if (LoggedWallet.currentlyLoggedWallet == null) {
            favCardView.visibility = View.GONE
        }

        if (viewModel.project.isFavorite) {
            projectLikeAnimationView.progress = 0.5f
        }

        favCardView.setOnClickListener {
            if (viewModel.project.isFavorite) {
                projectLikeAnimationView.progress = 0.5f
                projectLikeAnimationView.setMinAndMaxProgress(0.5f, 1.0f)
                projectLikeAnimationView.playAnimation()
                viewModel.removeFavorite()
            }
            else {
                projectLikeAnimationView.progress = 0.0f
                projectLikeAnimationView.setMinAndMaxProgress(0.0f, 0.5f)
                projectLikeAnimationView.playAnimation()
                viewModel.setFavorite()
            }
            viewModel.project.isFavorite = !viewModel.project.isFavorite
        }

        handleMotionLayoutTransitions()

        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        favCardView.transitionName = args.project.id + "_card"
        textTitle.transitionName = args.project.id + "_title"
        textType.transitionName = args.project.id + "_category"
        ((imageSlider[0] as RelativeLayout)[0] as ViewPager).transitionName = args.project.id + "_image"
    }

    private fun stopLoading() {
        loadingAnimation.visibility = View.GONE
        tasksRecyclerView.visibility = View.VISIBLE
    }

    private fun calcultateTotalAmount(it: List<Task>?) {
        var total = 0.0
        it?.forEach {
            total += it.amount
        }
        viewModel.project.totalAmount = total
        textToCollect.text = resources.getString(R.string.eth_suffix,
            BigDecimal(total.toString()).setScale(2, RoundingMode.HALF_UP).toString())
    }

    private fun handleMotionLayoutTransitions() {
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
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
