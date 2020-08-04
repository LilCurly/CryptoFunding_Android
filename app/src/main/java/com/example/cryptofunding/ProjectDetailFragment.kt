package com.example.cryptofunding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.ui.viewholder.TaskAdapter
import com.example.cryptofunding.utils.DEBUG
import kotlinx.android.synthetic.main.fragment_project_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProjectDetailFragment : Fragment() {
    lateinit var imageList: List<SlideModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = listOf(SlideModel(R.drawable.avengers_poster), SlideModel(R.drawable.avengers_poster), SlideModel(R.drawable.avengers_poster))
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        val task = Task("Ecrire le scénario", "fkdmfksdf mldsmfk smfkl smdlkf mskf smfkmsdkfsdmfksdfsdlkfjdskf jsldk kfsd jf", 50, "30/08/95")

        tasksRecyclerView.adapter = TaskAdapter(requireContext(), mutableListOf(task, task, task, task, task))
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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


}
