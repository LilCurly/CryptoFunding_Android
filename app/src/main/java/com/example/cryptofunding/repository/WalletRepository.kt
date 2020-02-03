package com.example.cryptofunding.repository

import com.example.cryptofunding.entity.Wallet
import com.example.cryptofunding.repository.dao.AbstractDao
import com.example.cryptofunding.repository.dao.FakeDao
import com.example.cryptofunding.repository.mapper.WalletMapper

object WalletRepository {
    private val dao: AbstractDao = FakeDao()

    var listOfWallets = mutableListOf<Wallet>()
        private set
    var currentWallet: Wallet? = null
        private set

    fun loadWallets(): List<Wallet> {
        val wallets = WalletMapper.fromDataListToEntityList(dao.loadWallets())
        listOfWallets = wallets.toMutableList()
        return wallets
    }

    fun registerWallet(wallet: Wallet) {
        dao.saveWallet(WalletMapper.fromEntityToData(wallet))
        listOfWallets.add(wallet)
    }

    fun getWalletForAddress(address: String): Wallet {
        return listOfWallets.first {
            address == it.publicKey
        }
    }

    fun setCurrentWallet(wallet: Wallet) {
        if (listOfWallets.contains(wallet)) {
            currentWallet = wallet
        }
    }
}