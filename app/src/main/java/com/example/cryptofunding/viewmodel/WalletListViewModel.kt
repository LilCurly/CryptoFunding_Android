package com.example.cryptofunding.viewmodel

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import androidx.lifecycle.*
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.ui.viewholder.WalletItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun setCurrentWallet(wallet: Wallet?) {
        repo.currentWallet = wallet
        currentWallet.value = wallet
    }

    fun loadAmountIfNeeded(wallet: Wallet) {
        if (wallet.amount.value == null) {
            wallet.loadAmount()
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWallet(wallet)
        }
    }

    fun addWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWallet(wallet)
        }
    }
}