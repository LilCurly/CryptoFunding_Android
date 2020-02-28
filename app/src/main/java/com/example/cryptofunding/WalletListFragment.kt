package com.example.cryptofunding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_wallet_list.*
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
        setupToolbar()
        hideDetails()

        viewModel.wallets.observe(this) {
            if (it.isNotEmpty()) {
                wallet_list_nowallet.visibility = View.GONE
                viewModel.setupWalletList(it)
                wallet_list_recyclerview.adapter = viewModel.adapter
                wallet_list_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            else {
                wallet_list_recyclerview.visibility = View.GONE
            }
        }

        viewModel.currentWallet.observe(this) {
            showDetails()

            wallet_list_detailname.text = it.name
            wallet_list_notifamount.text = "0"
            wallet_list_amount.text = it.getAmount()
        }
    }

    private fun showDetails() {
        wallet_list_noselected.visibility = View.GONE
        wallet_list_detailname.visibility = View.VISIBLE
        wallet_list_detailname.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        wallet_list_amount.visibility = View.VISIBLE
        wallet_list_amount.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        wallet_list_available.visibility = View.VISIBLE
        wallet_list_currency.visibility = View.VISIBLE
        wallet_list_notification.visibility = View.VISIBLE
        wallet_list_notifamount.visibility = View.VISIBLE
        wallet_list_notifamount.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }

    private fun hideDetails() {
        wallet_list_noselected.visibility = View.VISIBLE
        wallet_list_detailname.visibility = View.GONE
        wallet_list_amount.visibility = View.GONE
        wallet_list_available.visibility = View.GONE
        wallet_list_currency.visibility = View.GONE
        wallet_list_notification.visibility = View.GONE
        wallet_list_notifamount.visibility = View.GONE
    }

    private fun setupToolbar() {
        activity?.toolbar_title?.text = getString(R.string.my_wallets)
    }
}
