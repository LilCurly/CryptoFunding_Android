package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.adapter.NewWalletAdapter
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.isKeyboardOpen
import com.example.cryptofunding.viewmodel.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_new_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet_list.*

/**
 * A simple [Fragment] subclass.
 */
class NewWalletFragment : Fragment() {

    val viewModel by viewModel {
        activity!!.injector.newWalletViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = NewWalletAdapter(this)
        newWallet_pager.adapter = viewPagerAdapter

        view.viewTreeObserver.addOnGlobalLayoutListener(handleKeyboardOpening())

        TabLayoutMediator(newWallet_tablayout, newWallet_pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.create)
                1 -> tab.text = getString(R.string.import_text)
            }
        }.attach()
    }

    private fun handleKeyboardOpening() = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var opened = false
            override fun onGlobalLayout() {
                val isOpen = isKeyboardOpen()
                if (isOpen && !opened) {
                    newWallet_tablayout.animate()
                        .setInterpolator(LinearInterpolator())
                        .setStartDelay(0)
                        .setDuration(115)
                        .translationY(-275F)
                        .alpha(0F)
                        .setUpdateListener {
                            val constraintSet = ConstraintSet()
                            constraintSet.clone(newwallet_constraint)
                            constraintSet.connect(R.id.newWallet_pager, ConstraintSet.TOP, R.id.newwallet_constraint, ConstraintSet.TOP, 25)
                            constraintSet.applyTo(newwallet_constraint)
                        }
                        .start()
                    opened = true
                    return
                } else if (!isOpen && opened) {
                    newWallet_tablayout.animate()
                        .setInterpolator(LinearInterpolator())
                        .setStartDelay(0)
                        .setDuration(75)
                        .translationY(1F)
                        .alpha(1F)
                        .setUpdateListener {
                            val constraintSet = ConstraintSet()
                            constraintSet.clone(newwallet_constraint)
                            constraintSet.connect(R.id.newWallet_pager, ConstraintSet.TOP, R.id.newWallet_tablayout, ConstraintSet.BOTTOM, 0)
                            constraintSet.applyTo(newwallet_constraint)
                        }
                        .start()
                    opened = false
                }
            }
    }

}
