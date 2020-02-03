package com.example.cryptofunding.utils

import com.example.cryptofunding.entity.Wallet
import com.example.cryptofunding.repository.WalletRepository
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import java.lang.Exception

object WalletHandler {

    fun generateNewWalletFile(password: String, path: String): Wallet {
        val filePath = File(path)
        val fileName = WalletUtils.generateNewWalletFile(password, filePath)
        val fullPath = "$filePath/$fileName"

        val credentials = WalletUtils.loadCredentials(password, fullPath)
        val createdWallet = Wallet(credentials.address, fullPath)
        WalletRepository.registerWallet(createdWallet)

        return createdWallet
    }

    fun generateNewWalletFileFromPrivateKey(
        password: String,
        privateKey: String,
        path: String
    ): Wallet {
        val credentials = Credentials.create(privateKey)

        val filePath = File(path)
        val fileName =
            WalletUtils.generateWalletFile(password, credentials.ecKeyPair, filePath, false)
        val fullPath = "$filePath/$fileName"

        val createdWallet = Wallet(credentials.address, fullPath)
        WalletRepository.registerWallet(createdWallet)

        return createdWallet
    }

    fun loadWallets(): List<Wallet> {
        return WalletRepository.loadWallets()
    }

    fun loadCurrentWallet(wallet: Wallet, password: String): Wallet? {
        return try {
            WalletUtils.loadCredentials(password, wallet.pathToJSON)
            WalletRepository.setCurrentWallet(wallet)
            wallet
        } catch (e: CipherException) {
            null
        }
    }

    fun getCurrentWallet(): Wallet? {
        return WalletRepository.currentWallet
    }
}

class WalletHandlerException(error: String = ""): Exception(error)