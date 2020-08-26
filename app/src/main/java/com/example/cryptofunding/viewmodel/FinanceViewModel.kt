package com.example.cryptofunding.viewmodel

import androidx.lifecycle.LiveData
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.WalletRepository
import javax.inject.Inject

class FinanceViewModel @Inject constructor(private val repo: WalletRepository) {
    var wallets: LiveData<List<Wallet>> = repo.getAll()
    var currentWalletKey: String? = null
}