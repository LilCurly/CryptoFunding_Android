package com.example.cryptofunding.ui.custom

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.databinding.SheetAddTaskBinding
import com.example.cryptofunding.di.injector
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.sheet_add_task.*
import java.util.*


class AddTaskBottomSheet (val onDismiss: ((task: Task) -> Unit),
                          var editableTask: Task? = null): BottomSheetDialogFragment() {

    val viewModel by lazy {
        requireActivity().injector.addTaskViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SheetAddTaskBinding>(inflater, R.layout.sheet_add_task, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editableTask?.let {
            viewModel.taskName.postValue(it.title)
            viewModel.taskSummary.postValue(it.summary)
            viewModel.taskAmount.postValue(it.amount.toString())
            viewModel.taskFinalDate.postValue(it.limitDate)
        }

        dateEditText.setOnClickListener {
            setupDatePicker()
        }

        setupEditTextObservables()

        buttonAddTask.setOnClickListener {
            onDismiss(viewModel.getTask())
            dismiss()
        }
    }

    private fun setupEditTextObservables() {
        viewModel.taskAmount.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }

        viewModel.taskFinalDate.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }

        viewModel.taskName.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }

        viewModel.taskSummary.observe(viewLifecycleOwner) {
            viewModel.updateCanProceed()
        }
    }

    private fun setupDatePicker() {
        val now = Calendar.getInstance()
        lateinit var datePicker: DatePickerDialog
        if (dateEditText.text.toString().isEmpty()) {
            datePicker = DatePickerDialog.newInstance(
                { _, year, month, day ->
                    dateEditText.text =
                        Editable.Factory.getInstance().newEditable(dateFormatIfNeeded(day, month, year))
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
        }
        else {
            val date = dateEditText.text.toString()
            val day = date.subSequence(0, 2).toString().toInt()
            val month = date.subSequence(3, 5).toString().toInt()
            val year = date.subSequence(6, date.length).toString().toInt()
            datePicker = DatePickerDialog.newInstance(
                { _, year, month, day ->
                    dateEditText.text =
                        Editable.Factory.getInstance().newEditable(dateFormatIfNeeded(day, month, year))
                },
                year,
                month - 1,
                day
            )
        }

        datePicker.accentColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryApp)
        datePicker.isThemeDark = true
        datePicker.setOkColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBackgroundWhiteApp
            )
        )
        datePicker.setCancelColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBackgroundWhiteApp
            )
        )
        datePicker.minDate = now
        datePicker.locale = Locale.FRANCE
        datePicker.show(parentFragmentManager, "TAG")
    }

    private fun dateFormatIfNeeded(day: Int, month: Int, year: Int): String {
        var formattedDay = day.toString()
        var formattedMonth = (month + 1).toString()

        if (formattedDay.length == 1) {
            formattedDay = "0$formattedDay"
        }
        if (formattedMonth.length == 1) {
            formattedMonth = "0$formattedMonth"
        }

        return "$formattedDay/$formattedMonth/$year"
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog

        dialog?.let {
            val bottomSheet: FrameLayout = it.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}