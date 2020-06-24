package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.adapter.NewWalletAdapter
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.isKeyboardOpen
import com.example.cryptofunding.viewmodel.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_new_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet_list.*

/**
 * A simple [Fragment] subclass.
 */
class NewWalletFragment : Fragment() {

    val viewModel by viewModel {
        requireActivity().injector.newWalletViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        return inflater.inflate(R.layout.fragment_new_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        val viewPagerAdapter = NewWalletAdapter(this)
        newWallet_pager.adapter = viewPagerAdapter

        //view.viewTreeObserver.addOnGlobalLayoutListener(handleKeyboardOpening())

        TabLayoutMediator(newWallet_tablayout, newWallet_pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.create)
                1 -> tab.text = getString(R.string.import_text)
            }
        }.attach()
    }

    private fun setupToolbar() {
        activity?.toolbarTitle?.text = resources.getString(R.string.create_wallet)
        activity?.walletImageView?.visibility = View.GONE
        activity?.closeImageView?.visibility = View.VISIBLE
        activity?.closeImageView?.setOnClickListener {
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    fun popToWalletList() {
        findNavController().popBackStack()
    }

}
