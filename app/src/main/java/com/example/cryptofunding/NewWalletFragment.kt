package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.adapter.NewWalletAdapter
import com.example.cryptofunding.viewmodel.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_new_wallet.*

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

        TabLayoutMediator(newWallet_tablayout, newWallet_pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.create)
                1 -> tab.text = getString(R.string.import_text)
            }
        }.attach()
    }

}
