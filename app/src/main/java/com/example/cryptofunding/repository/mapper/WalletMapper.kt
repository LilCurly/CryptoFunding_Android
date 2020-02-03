package com.example.cryptofunding.repository.mapper

import com.example.cryptofunding.entity.Wallet
import com.example.cryptofunding.repository.data.WalletData

object WalletMapper {

    fun fromEntityToData(wallet: Wallet): WalletData {
        return WalletData(wallet.publicKey, wallet.pathToJSON)
    }

    fun fromDataToEntity(wallet: WalletData): Wallet {
        return Wallet(wallet.publicKey, wallet.jsonPath)
    }

    fun fromEntityListToDataList(walletList: List<Wallet>): List<WalletData> {
        return walletList.map {
            WalletData(it.publicKey, it.pathToJSON)
        }
    }

    fun fromDataListToEntityList(walletList: List<WalletData>): List<Wallet> {
        return walletList.map {
            Wallet(it.publicKey, it.jsonPath)
        }
    }

}