package com.example.cryptofunding.ui.custom

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.cryptofunding.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.sheet_add_task.*
import java.util.*


class AddTaskBottomSheet: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateEditText.setOnClickListener {
            setupDatePicker()
        }
    }

    private fun setupDatePicker() {
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
            { _, year, month, day ->
                dateEditText.text =
                    Editable.Factory.getInstance().newEditable(dateFormatIfNeeded(day, month, year))
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )

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
        var formatedDay = day.toString()
        var formatedMonth = (month + 1).toString()

        if (formatedDay.length == 1) {
            formatedDay = "0$formatedDay"
        }
        if (formatedMonth.length == 1) {
            formatedMonth = "0$formatedMonth"
        }

        return "$formatedDay/$formatedMonth/$year"
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