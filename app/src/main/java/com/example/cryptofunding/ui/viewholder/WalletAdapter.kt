package com.example.cryptofunding.ui.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.daimajia.swipe.util.Attributes
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Wallet
import kotlinx.android.synthetic.main.item_wallet.view.*

open class WalletAdapter(val context: Context,
                         private val walletList: List<Wallet>,
                         val onClickListener: (view: View, viewHolder: ViewHolder, item: Wallet, index: Int) -> Unit
): RecyclerSwipeAdapter<WalletAdapter.ViewHolder>() {
    var clickedPosition: Int = -1

    init {
        mItemManger.mode = Attributes.Mode.Single
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)
        return ViewHolder(view)
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val wallet = walletList[position]
        mItemManger.bindView(viewHolder.swipeLayout, position)

        if (mItemManger.isOpen(position)) {
            viewHolder.swipeLayout.open(false)
        }

        if (clickedPosition == position) {
            viewHolder.scaleUpView()
        }

        wallet.amount.observe(viewHolder.view.context as LifecycleOwner) {
            viewHolder.amount.text = it
            viewHolder.stopAnimation()
        }

        viewHolder.address.text = wallet.publicKey
        viewHolder.name.text = wallet.name

        viewHolder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        viewHolder.swipeLayout.setOnClickListener {
            if (viewHolder.swipeLayout.openStatus == SwipeLayout.Status.Close) {
                clickedPosition = if (clickedPosition == position) -1 else position
                onClickListener(viewHolder.itemView, viewHolder, walletList[position], position)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.name.text = null
        holder.address.text = null

        holder.scaleDownView()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val swipeLayout: SwipeLayout = view.swipeLayout
        val address: TextView = view.walletitem_address
        val amount: TextView = view.walletitem_amount
        val name: TextView = view.walletitem_name
        val currency: TextView = view.walletitem_currency
        val animation: LottieAnimationView = view.loading_item_amount_animation

        fun scaleUpView() {
            itemView.animate()
                .setDuration(0)
                .setStartDelay(0)
                .setListener(null)
                .setInterpolator(LinearInterpolator())
                .scaleX(1.04f)
                .scaleY(1.04f)
                .start()
        }

        fun scaleDownView() {
            itemView.animate()
                .setDuration(0)
                .setStartDelay(0)
                .setListener(null)
                .setInterpolator(LinearInterpolator())
                .scaleX(1f)
                .scaleY(1f)
                .start()
        }

        fun stopAnimation() {
            animation.visibility = View.GONE
            amount.visibility = View.VISIBLE
            amount.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.fade_in))
            currency.visibility = View.VISIBLE
            currency.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.fade_in))
        }
    }
}