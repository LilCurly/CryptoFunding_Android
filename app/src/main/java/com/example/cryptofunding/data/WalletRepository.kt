package com.example.cryptofunding.data

import androidx.lifecycle.LiveData
import io.reactivex.Single

interface WalletRepository {
    var currentWallet: Wallet?
    fun getAll(): LiveData<List<Wallet>>
    fun getByPublicKey(publicKey: String): LiveData<Wallet>
    fun getByName(name: String): LiveData<Wallet>
    fun insertWallet(wallet: Wallet): Single<Long>
    fun deleteWallet(wallet: Wallet)
    fun exists(publicKey: String): Wallet?
}