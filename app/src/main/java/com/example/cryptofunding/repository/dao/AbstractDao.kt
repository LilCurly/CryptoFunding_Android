package com.example.cryptofunding.repository.dao

import com.example.cryptofunding.entity.Wallet

interface AbstractDao {
    fun saveWallet(wallet: Wallet): Boolean
    fun removeWallet(wallet: Wallet): Boolean
    fun loadWallets(): List<Wallet>
}