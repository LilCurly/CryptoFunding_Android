package com.example.cryptofunding.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.DefaultWalletRepository
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.data.WalletRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.WalletUtils
import java.io.File
import java.lang.Exception
import java.util.*

object WalletHandler {

    fun generateNewWalletFile(
        password: String,
        name: String,
        path: String,
        repository: WalletRepository
    ): LiveData<Result<Wallet>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading<Wallet>())
            try {
                val filePath = File(path)
                val ecKeyPair = Keys.createEcKeyPair()
                val fileName = WalletUtils.generateWalletFile(password, ecKeyPair, filePath, false)
                val fullPath = "$filePath/$fileName"

                val credentials = WalletUtils.loadCredentials(password, fullPath)
                val createdWallet = Wallet(credentials.address, name, fullPath)
                repository.insertWallet(createdWallet)
                emit(Result.success(createdWallet))
            } catch (ex: Exception) {
                emit(Result.error("Une erreur est survenue"))
            }
        }

    fun generateNewWalletFileFromPrivateKey(
        password: String,
        name: String,
        privateKey: String,
        path: String,
        repository: WalletRepository
    ): LiveData<Result<Wallet>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading<Wallet>())
            try {
                val credentials = Credentials.create(privateKey)

                val filePath = File(path)
                val fileName =
                    WalletUtils.generateWalletFile(password, credentials.ecKeyPair, filePath, false)
                val fullPath = "$filePath/$fileName"

                val createdWallet = Wallet(credentials.address, name, fullPath)

                if (repository.exists(createdWallet.publicKey) == null) {
                    repository.insertWallet(createdWallet)
                    emit(Result.success(createdWallet))
                } else {
                    emit(Result.error("Un portefeuille pour cette clé est déjà enregistré"))
                }
            } catch (ex: Exception) {
                emit(Result.error("Une erreur est survenue"))
            }
        }

    fun loadCurrentWallet(wallet: Wallet, password: String, repository: DefaultWalletRepository): Wallet? {
        return try {
            WalletUtils.loadCredentials(password, wallet.jsonPath)
            LoggedWallet.currentlyLoggedWallet = wallet
            wallet
        } catch (e: CipherException) {
            null
        }
    }

    fun getPrivateKey(password: String, path: String): LiveData<Result<String>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading())
            try {
                val credentials = WalletUtils.loadCredentials(password, path)
                val pk = credentials.ecKeyPair.privateKey.toString(16).toUpperCase(Locale.ROOT)
                emit(Result.success(pk))
            } catch (ex: CipherException) {
                emit(Result.error("Wrong password"))
            }
        }
    }

class WalletHandlerException(error: String = ""): Exception(error)