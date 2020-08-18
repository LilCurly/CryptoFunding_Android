package com.example.cryptofunding.data

import androidx.lifecycle.LiveData
import io.reactivex.Single

interface WalletRepository {
    fun getAll(): LiveData<List<Wallet>>
    fun getByPublicKey(publicKey: String): LiveData<Wallet>
    fun getByName(name: String): LiveData<Wallet>
    fun insertWallet(wallet: Wallet)
    fun deleteWallet(wallet: Wallet)
    fun exists(publicKey: String): Wallet?
}