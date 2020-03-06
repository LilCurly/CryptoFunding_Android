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

    fun isCurrentWallet(wallet: Wallet): Boolean {
        repo.currentWallet?.let {
            if (it == wallet) {
                return true
            }
        }
        return false
    }

    fun setCurrentWallet(wallet: Wallet) {
        repo.currentWallet = wallet
        currentWallet.value = wallet
    }

    fun loadAmountIfNeeded(wallet: Wallet) {
        if (wallet.amount.value == null) {
            wallet.loadAmount()
        }
    }
}