package com.example.cryptofunding.viewmodel

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.ui.viewholder.WalletItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import javax.inject.Inject

class WalletListViewModel @Inject  constructor(private val repo: WalletRepository) : ViewModel() {
    val wallets: LiveData<List<Wallet>> = repo.getAll()
    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    lateinit var adapter: FastAdapter<WalletItem>
    private var currentSelectedView: View? = null
    private var itemPosition: Int? = null

    fun setupWalletList(walletList: List<Wallet>) {
        val itemAdapter = ItemAdapter<WalletItem>()
        adapter = FastAdapter.with(itemAdapter)

        itemAdapter.add(walletList.map {
            it.loadAmount()
            val item = WalletItem(it)
            repo.currentWallet?.let { currentWallet ->
                if (it == currentWallet) {
                    item.isSelected = true
                }
            }
            item
        })

        adapter.onClickListener = { view, adapter, item, index ->
            if (repo.currentWallet == null || item.wallet != repo.currentWallet) {
                itemPosition?.let {
                    adapter.getAdapterItem(it).isSelected = false
                }
                itemPosition = index
                setCurrentWallet(item.wallet)
                currentWallet.value = item.wallet
                deselectRow()
                item.isSelected = true
                currentSelectedView = view
                createScaleAnimation(view)
            }

            true
        }
    }

    private fun setCurrentWallet(wallet: Wallet) {
        repo.currentWallet = wallet
    }

    private fun deselectRow() {
        currentSelectedView?.let {
            it.animation = null
            it.animate()
                .setDuration(50)
                .scaleY(1f)
                .scaleX(1f)
                .setStartDelay(10)
                .setInterpolator(LinearInterpolator())
                .setListener(null)
                .start()
        }
    }

    private fun createScaleAnimation(view: View?) {
        view?.let {
            val interpolator = LinearInterpolator()
            it.animate()
                .setDuration(50)
                .scaleX(1.06f)
                .scaleY(1.06f)
                .setStartDelay(10)
                .setInterpolator(interpolator)
                .setListener(object: AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        it.animate()
                            .setDuration(100)
                            .scaleX(1.04f)
                            .scaleY(1.04f)
                            .setInterpolator(interpolator)
                            .start()
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }

                })
                .start()
        }
    }
}