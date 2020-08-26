package com.example.cryptofunding.ui.viewholder

import android.content.ContextWrapper
import android.view.ContextThemeWrapper
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.airbnb.lottie.LottieAnimationView
import com.daimajia.swipe.SwipeLayout
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Wallet
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletItem(val wallet: Wallet): AbstractItem<WalletItem.ViewHolder>() {
    val name: String = wallet.name
    val address: String? = wallet.publicKey

    override val layoutRes: Int
        get() = R.layout.item_wallet_foreground
    override val type: Int
        get() = R.id.walletItem

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(val view: View): FastAdapter.ViewHolder<WalletItem>(view) {
        private val address: TextView = view.walletitem_address
        private val amount: TextView = view.walletitem_amount
        private val name: TextView = view.walletitem_name
        private val animation: LottieAnimationView = view.loading_item_amount_animation
        private val currency: TextView = view.walletitem_currency

        override fun bindView(item: WalletItem, payloads: List<Any>) {
            address.text = item.address
            name.text = item.name
            item.wallet.amount.observe((view.context as ContextWrapper).baseContext as LifecycleOwner) {
                amount.text = it
                stopAnimation()
            }
        }

        override fun unbindView(item: WalletItem) {
            address.text = null
            amount.text = null
            name.text = null
            itemView.animation = null
        }

        private fun stopAnimation() {
            animation.visibility = View.GONE
            amount.visibility = View.VISIBLE
            amount.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.fade_in))
            currency.visibility = View.VISIBLE
            currency.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.fade_in))
        }
    }
}