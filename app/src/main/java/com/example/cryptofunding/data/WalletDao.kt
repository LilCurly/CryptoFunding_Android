package com.example.cryptofunding.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WalletDao: AbstractWalletDao {
    @Query("SELECT * FROM wallet")
    override fun getAll(): LiveData<List<Wallet>>

    @Query("SELECT * FROM wallet WHERE id = :publicKey")
    override fun getByPublicKey(publicKey: String): LiveData<Wallet>

    @Query("SELECT * FROM wallet WHERE name = :name")
    override fun getByName(name: String): LiveData<Wallet>

    @Insert
    override fun insertWallet(wallet: Wallet)

    @Delete
    override fun deleteWallet(wallet: Wallet)
}