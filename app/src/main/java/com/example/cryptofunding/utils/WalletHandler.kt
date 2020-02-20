package com.example.cryptofunding.utils

import android.util.Log
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.WalletRepository
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import java.lang.Exception

object WalletHandler {

    fun generateNewWalletFile(
        password: String,
        name: String,
        path: String,
        repository: WalletRepository
    ): Wallet {
        val filePath = File(path)
        val fileName = WalletUtils.generateNewWalletFile(password, filePath)
        val fullPath = "$filePath/$fileName"

        val credentials = WalletUtils.loadCredentials(password, fullPath)
        val createdWallet = Wallet(credentials.address, name, fullPath)
        repository.insertWallet(createdWallet)

        return createdWallet
    }

    fun generateNewWalletFileFromPrivateKey(
        password: String,
        name: String,
        privateKey: String,
        path: String,
        repository: WalletRepository
    ): Wallet {
        val credentials = Credentials.create(privateKey)

        val filePath = File(path)
        val fileName =
            WalletUtils.generateWalletFile(password, credentials.ecKeyPair, filePath, false)
        val fullPath = "$filePath/$fileName"

        val createdWallet = Wallet(credentials.address, name, fullPath)

        if (repository.getByPublicKey(createdWallet.publicKey) == null) {
            repository.insertWallet(createdWallet)
        } else {
            Log.d(DEBUG, "Wallet already exists")
        }

        return createdWallet
    }

    fun loadWallets(repository: WalletRepository): List<Wallet> {
        return repository.getAll()
    }

    fun loadCurrentWallet(wallet: Wallet, password: String, repository: WalletRepository): Wallet? {
        return try {
            WalletUtils.loadCredentials(password, wallet.jsonPath)
            repository.currentWallet = wallet
            wallet
        } catch (e: CipherException) {
            null
        }
    }

    fun getCurrentWallet(repository: WalletRepository): Wallet? {
        return repository.currentWallet
    }
}

class WalletHandlerException(error: String = ""): Exception(error)