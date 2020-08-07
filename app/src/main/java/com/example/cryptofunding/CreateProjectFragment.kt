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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.cryptofunding.data.repository.ProjectRepository
import com.example.cryptofunding.databinding.FragmentCreateProjectBinding
import com.example.cryptofunding.ui.custom.FractionLinearLayoutManager
import com.example.cryptofunding.ui.viewholder.CategoryItem
import com.example.cryptofunding.ui.viewholder.SmallImageItem
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.NewProjectViewModel
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

    private val viewModel: NewProjectViewModel by lazy {
        ViewModelProvider(CryptoFundingApplication(), defaultViewModelProviderFactory).get(NewProjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateProjectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_project, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateCanProceed()
        setupTypePicker()
        setupImageRecyclerView()
        listenOnEditText()

        buttonNext.setOnClickListener {
            val action = CreateProjectFragmentDirections.actionCreateProjectFragmentToAddTasksFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun listenOnEditText() {
        viewModel.projectName.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }

        viewModel.projectSummary.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }
    }

    private fun setupImageRecyclerView() {
        imagesRecyclerView.adapter = imageFastAdapter
        imagesRecyclerView.layoutManager = FractionLinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            0.2f,
            false
        )

        if (imageItemAdapter.adapterItems.isEmpty()) {
            imageItemAdapter.add(SmallImageItem(
                    SmallImageItem.Companion.ItemType.ADD_IMAGE,
                    null,
                    {
                        launchImagePicker()
                    }
                )
            )
        }
    }

    private fun setupTypePicker() {
        typePicker.adapter = categoryFastAdapter
        val pickerLayoutManager =
            PickerLayoutManager(requireContext(), PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManager.isChangeAlpha = true
        pickerLayoutManager.scaleDownBy = 0.65f
        pickerLayoutManager.scaleDownDistance = 0.8f
        typePicker.layoutManager = pickerLayoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(typePicker)

        if (categoryItemAdapter.adapterItems.isEmpty()) {
            categoryItemAdapter.add(ProjectRepository.categories.map {
                CategoryItem(it)
            })
        }

        typePicker.smoothScrollBy(1, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            viewModel.projectImages.addAll(ImagePicker.getImages(data))
            viewModel.updateCanProceed()
            reloadImageData()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun reloadImageData() {
        imageItemAdapter.clear()
        imageItemAdapter.add(viewModel.projectImages.map {
            val imgPath = File(it.path)
            var bitmap: Bitmap? = null
            if (imgPath.exists()) {
                bitmap = BitmapFactory.decodeFile(imgPath.absolutePath)
            }
            SmallImageItem(SmallImageItem.Companion.ItemType.SMALL_IMAGE,
                bitmap,
                null,
                { index ->
                    viewModel.projectImages.removeAt(index)
                    viewModel.updateCanProceed()
                    imageItemAdapter.remove(index)
                    imageFastAdapter.notifyAdapterItemRemoved(index)
                    imageFastAdapter.notifyAdapterItemRangeChanged(index, imageFastAdapter.itemCount)

                    if (viewModel.projectImages.size == 4) {
                        imageItemAdapter.add(SmallImageItem(
                                SmallImageItem.Companion.ItemType.ADD_IMAGE,
                                null,
                                {
                                    launchImagePicker()
                                }
                            )
                        )
                    }
                })
        })

        if (viewModel.projectImages.size < 5) {
            imageItemAdapter.add(SmallImageItem(
                    SmallImageItem.Companion.ItemType.ADD_IMAGE,
                    null,
                    {
                        launchImagePicker()
                    }
                )
            )
        }
    }

    private fun launchImagePicker() {
        ImagePicker.create(this)
            .limit(5 - viewModel.projectImages.size)
            .start()
    }
}
