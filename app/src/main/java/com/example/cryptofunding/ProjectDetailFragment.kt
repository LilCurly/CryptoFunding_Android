package com.example.cryptofunding


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
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
        imageList = listOf(SlideModel(R.drawable.avengers_poster), SlideModel(R.drawable.avengers_poster), SlideModel(R.drawable.avengers_poster))
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        super.onViewCreated(view, savedInstanceState)
    }


}
