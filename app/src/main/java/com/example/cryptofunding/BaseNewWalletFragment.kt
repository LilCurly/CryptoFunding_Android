package com.example.cryptofunding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_new_wallet.*

open class BaseNewWalletFragment: Fragment() {
    var tabTouchables = mutableListOf<View>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun disableNavigation() {
        (parentFragment as NewWalletFragment).newWallet_pager.isUserInputEnabled = false
        tabTouchables = (parentFragment as NewWalletFragment).newWallet_tablayout.touchables
        tabTouchables.forEach {
            it.isEnabled = false
        }
        (parentFragment as NewWalletFragment).newWallet_tablayout
            .setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorSecondaryAppLighter))
    }

    fun enableNavigation() {
        (parentFragment as NewWalletFragment).newWallet_pager.isUserInputEnabled = true
        tabTouchables.forEach {
            it.isEnabled = true
        }
        (parentFragment as NewWalletFragment).newWallet_tablayout
            .setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorBackgroundDarkApp))
    }
}