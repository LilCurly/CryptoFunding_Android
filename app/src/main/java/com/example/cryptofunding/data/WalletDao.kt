package com.example.cryptofunding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WalletDao: AbstractWalletDao {
    @Query("SELECT * FROM wallet")
    override fun getAll(): List<Wallet>

    @Query("SELECT * FROM wallet WHERE id = :publicKey")
    override fun getByPublicKey(publicKey: String): Wallet?

    @Query("SELECT * FROM wallet WHERE name = :name")
    override fun getByName(name: String): Wallet?

    @Query("SELECT * FROM wallet WHERE id = :publicKey")
    override fun exists(publicKey: String): Wallet?

    @Insert
    override fun insertWallet(wallet: Wallet)

    @Delete
    override fun deleteWallet(wallet: Wallet)
}