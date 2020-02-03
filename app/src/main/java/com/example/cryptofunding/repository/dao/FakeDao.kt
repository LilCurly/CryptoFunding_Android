package com.example.cryptofunding.repository.dao

import com.example.cryptofunding.entity.Wallet

class FakeDao : AbstractDao {
    private val listOfWallets = mutableListOf<Wallet>()

    override fun saveWallet(wallet: Wallet): Boolean {
        listOfWallets.add(wallet)
        return true
    }

    override fun removeWallet(wallet: Wallet): Boolean {
        if (listOfWallets.contains(wallet)) {
            listOfWallets.remove(wallet)
            return true
        }
        return false
    }

    override fun loadWallets(): List<Wallet> {
        return listOfWallets
    }

}