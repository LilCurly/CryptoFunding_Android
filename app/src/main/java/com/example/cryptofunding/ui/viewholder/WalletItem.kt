package com.example.cryptofunding.ui.viewholder

import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Wallet
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletItem(val wallet: Wallet): AbstractItem<WalletItem.ViewHolder>() {
    val name: String = wallet.name
    val address: String? = wallet.publicKey
    val amount: String? = wallet.loadAmount()

    override val layoutRes: Int
        get() = R.layout.item_wallet
    override val type: Int
        get() = R.id.walletitem_name

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(private val view: View): FastAdapter.ViewHolder<WalletItem>(view) {
        private val address: TextView = view.walletitem_address
        private val amount: TextView = view.walletitem_amount
        private val name: TextView = view.walletitem_name

        override fun bindView(item: WalletItem, payloads: MutableList<Any>) {
            amount.text = item.amount
            address.text = item.address
            name.text = item.name

            if (item.isSelected) {
                view.animate()
                    .setDuration(0)
                    .setStartDelay(0)
                    .setListener(null)
                    .setInterpolator(LinearInterpolator())
                    .scaleX(1.04f)
                    .scaleY(1.04f)
                    .start()
            }
        }

        override fun unbindView(item: WalletItem) {
            address.text = null
            amount.text = null
            name.text = null

            if (item.isSelected) {
                view.animate()
                    .setDuration(0)
                    .setStartDelay(0)
                    .setListener(null)
                    .setInterpolator(LinearInterpolator())
                    .scaleX(1f)
                    .scaleY(1f)
                    .start()
            }
        }

    }
}