package com.example.cryptofunding.data

interface AbstractWalletDao {
    fun getAll(): List<Wallet>
    fun getByPublicKey(publicKey: String): Wallet?
    fun getByName(name: String): Wallet?
    fun insertWallet(wallet: Wallet)
    fun deleteWallet(wallet: Wallet)
    fun exists(publicKey: String): Wallet?
}