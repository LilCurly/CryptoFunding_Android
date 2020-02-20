package com.example.cryptofunding.data

class WalletRepository private constructor(private val walletDao: AbstractWalletDao) {
    var currentWallet: Wallet? = null

    fun getAll() = walletDao.getAll()
    fun getByPublicKey(publicKey: String) = walletDao.getByPublicKey(publicKey)
    fun getByName(name: String) = walletDao.getByName(name)
    fun insertWallet(wallet: Wallet) = walletDao.insertWallet(wallet)
    fun deleteWallet(wallet: Wallet) = walletDao.deleteWallet(wallet)

    companion object {
        @Volatile private var instance: WalletRepository? = null

        fun getInstance(walletDao: AbstractWalletDao): WalletRepository {
            return instance ?: synchronized(this) {
                instance ?: WalletRepository(walletDao).also { instance = it }
            }
        }
    }
}