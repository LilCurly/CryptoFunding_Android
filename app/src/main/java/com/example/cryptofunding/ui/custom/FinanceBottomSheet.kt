package com.example.cryptofunding.ui.custom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.cryptofunding.R
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.WalletItem
import com.example.cryptofunding.utils.DEBUG
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_create_project.*
import kotlinx.android.synthetic.main.item_wallet_foreground.view.*
import kotlinx.android.synthetic.main.sheet_finance.*
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager

class FinanceBottomSheet(val onDismiss: () -> Unit): BottomSheetDialogFragment() {

    private val itemAdapter = ItemAdapter<WalletItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)
    private lateinit var pickerLayoutManager: PickerLayoutManager

    private val viewModel by lazy {
        requireActivity().injector.financeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_finance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomButton.setOnClickListener {
            motionLayout.setTransition(R.id.firstState, R.id.secondState)
            motionLayout.setTransitionDuration(300)
            motionLayout.transitionToEnd()
        }

        walletPicker.adapter = fastAdapter

        pickerLayoutManager = PickerLayoutManager(requireContext(), PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManager.isChangeAlpha = true
        pickerLayoutManager.scaleDownBy = 0.15f
        pickerLayoutManager.scaleDownDistance = 0.45f

        pickerLayoutManager.setOnScrollStopListener {
            viewModel.currentWalletKey = it.walletitem_address.text.toString()
            Log.d(DEBUG, viewModel.currentWalletKey)
        }

        walletPicker.layoutManager = pickerLayoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(walletPicker)

        walletPicker.smoothScrollBy(1, 0)

        viewModel.wallets.observe(viewLifecycleOwner) { walletList ->
            itemAdapter.add(walletList.map {
                it.loadAmount()
                WalletItem(it)
            })
        }
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet: FrameLayout = requireDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()
        onDismiss()
    }
}