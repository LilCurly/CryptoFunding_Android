package com.example.cryptofunding.data

import javax.inject.Inject

class DefaultWalletRepository @Inject internal constructor(private val walletDao: AbstractWalletDao): WalletRepository {
    override var currentWallet: Wallet? = null

    override fun getAll() = walletDao.getAll()
    override fun getByPublicKey(publicKey: String) = walletDao.getByPublicKey(publicKey)
    override fun getByName(name: String) = walletDao.getByName(name)
    override fun insertWallet(wallet: Wallet) = walletDao.insertWallet(wallet)
    override fun deleteWallet(wallet: Wallet) = walletDao.deleteWallet(wallet)

    companion object {
        @Volatile private var instance: DefaultWalletRepository? = null

        fun getInstance(walletDao: AbstractWalletDao): DefaultWalletRepository {
            return instance ?: synchronized(this) {
                instance ?: DefaultWalletRepository(walletDao).also { instance = it }
            }
        }
    }
}