package com.example.cryptofunding.repository.dao

import com.example.cryptofunding.repository.data.WalletData

interface AbstractDao {
    fun saveWallet(wallet: WalletData): Boolean
    fun removeWallet(wallet: WalletData): Boolean
    fun loadWallets(): List<WalletData>
}