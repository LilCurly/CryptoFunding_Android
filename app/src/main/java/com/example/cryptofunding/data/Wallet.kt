package com.example.cryptofunding.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey @ColumnInfo(name = "id") val publicKey: String,
    val name: String,
    val jsonPath: String) {

    override fun equals(other: Any?): Boolean {
        val oWallet = other as Wallet
        return oWallet.publicKey == publicKey
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}