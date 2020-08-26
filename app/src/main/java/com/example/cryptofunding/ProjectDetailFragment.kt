package com.example.cryptofunding


import android.animation.TimeInterpolator
import android.app.SharedElementCallback
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptofunding.data.SliderImage
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.databinding.FragmentProjectDetailBinding
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.adapter.SliderAdapter
import com.example.cryptofunding.ui.custom.FinanceBottomSheet
import com.example.cryptofunding.ui.viewholder.TaskAdapter
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.LoggedWallet
import kotlinx.android.synthetic.main.fragment_project_detail.*
import com.example.cryptofunding.viewmodel.viewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
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
        mutableListOf<SliderImage>()
    }

    private var prevState: Int = 0
    private var currentState: Int = 0

    private val args: ProjectDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.project = args.project

        setEnterSharedElementCallback(object: androidx.core.app.SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                handleMotionLayout()
            }
        })

        viewModel.loadTasksForId(viewModel.project.id!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        (sharedElementEnterTransition as TransitionSet).duration = 400
        (sharedElementEnterTransition as TransitionSet).interpolator = DecelerateInterpolator()
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        val binding: FragmentProjectDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasks.observe(viewLifecycleOwner) {
            calcultateTotalAmount(it)
            setupTasks(it.toMutableList())
            stopLoading()
        }

        hideToolbar()

        viewModel.project.imagesUrl.forEachIndexed { index, url ->
            if (index == 0) {
                imageList.add(SliderImage(url, args.posterBitmap))
            }
            else {
                imageList.add(SliderImage(url))
            }
        }
        sliderView.setSliderAdapter(SliderAdapter(requireContext(), imageList))
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.setInfiniteAdapterEnabled(false)

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

        sliderView[0].transitionName = args.project.id + "_image"

        financeButton.setOnClickListener {
            if (motionLayout.currentState == R.id.start) {
                prevState = R.id.start
                currentState = R.id.startClicked
                motionLayout.setTransition(R.id.start, R.id.startClicked)
            }
            else if (motionLayout.currentState == R.id.end) {
                prevState = R.id.end
                currentState = R.id.endClicked
                motionLayout.setTransition(R.id.end, R.id.endClicked)
            }
            motionLayout.setTransitionDuration(250)
            motionLayout.transitionToEnd()

            val bottomSheet = FinanceBottomSheet {
                motionLayout.setTransition(currentState, prevState)
                motionLayout.setTransitionDuration(250)
                motionLayout.transitionToEnd()
            }
            bottomSheet.show(parentFragmentManager, "TAG")
        }
    }

    private fun handleMotionLayout() {
        motionLayout.setTransition(R.id.opened, R.id.start)
        motionLayout.setTransitionDuration(800)
        motionLayout.transitionToEnd()
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
