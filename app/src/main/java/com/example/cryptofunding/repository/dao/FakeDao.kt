package com.example.cryptofunding.repository.dao

import com.example.cryptofunding.repository.data.WalletData

class FakeDao : AbstractDao {
    private val listOfWallets = mutableListOf<WalletData>()

    override fun saveWallet(wallet: WalletData): Boolean {
        listOfWallets.add(wallet)
        return true
    }

    override fun removeWallet(wallet: WalletData): Boolean {
        if (listOfWallets.contains(wallet)) {
            listOfWallets.remove(wallet)
            return true
        }
        return false
    }

    override fun loadWallets(): List<WalletData> {
        return listOfWallets
    }

}