package com.example.cryptofunding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.fragment_wallet_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class WalletListFragment : Fragment() {

    private val viewModel by viewModel {
        activity!!.injector.walletListViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wallets.observe(this) {
            viewModel.setupWalletList(it)
            view.wallet_list_recyclerview.adapter = viewModel.adapter
            view.wallet_list_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }


}
