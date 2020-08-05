package com.example.cryptofunding

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.data.repository.ProjectRepository
import com.example.cryptofunding.ui.custom.FractionLinearLayoutManager
import com.example.cryptofunding.ui.viewholder.CategoryItem
import com.example.cryptofunding.ui.viewholder.SmallImageItem
import com.example.cryptofunding.utils.DEBUG
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_create_project.*
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class CreateProjectFragment : Fragment() {

    private val categoryItemAdapter = ItemAdapter<CategoryItem>()
    private val categoryFastAdapter = FastAdapter.with(categoryItemAdapter)
    private val imageItemAdapter = ItemAdapter<SmallImageItem>()
    private val imageFastAdapter = FastAdapter.with(imageItemAdapter)

    private val images: MutableList<Image> by lazy {
        mutableListOf<Image>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typePicker.adapter = categoryFastAdapter
        val pickerLayoutManager = PickerLayoutManager(requireContext(), PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManager.isChangeAlpha = true
        pickerLayoutManager.scaleDownBy = 0.65f
        pickerLayoutManager.scaleDownDistance = 0.8f
        typePicker.layoutManager = pickerLayoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(typePicker)

        categoryItemAdapter.add(ProjectRepository.categories.map {
            CategoryItem(it)
        })

        typePicker.smoothScrollBy(1, 0)

        imagesRecyclerView.adapter = imageFastAdapter
        imagesRecyclerView.layoutManager = FractionLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, 0.2f, false)

        imageItemAdapter.add(SmallImageItem(
                SmallImageItem.Companion.ItemType.ADD_IMAGE,
                null
            ) {
                ImagePicker.create(this)
                    .limit(5 - images.size)
                    .start()
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images.addAll(ImagePicker.getImages(data))
            imageItemAdapter.clear()
            imageItemAdapter.add(images.map {
                val imgPath = File(it.path)
                var bitmap: Bitmap? = null
                if (imgPath.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgPath.absolutePath)
                }
                SmallImageItem(SmallImageItem.Companion.ItemType.SMALL_IMAGE, bitmap)
            })

            if (images.size < 5) {
                imageItemAdapter.add(SmallImageItem(
                    SmallImageItem.Companion.ItemType.ADD_IMAGE,
                    null
                ) {
                    ImagePicker.create(this)
                        .limit(5 - images.size)
                        .start()
                })
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
