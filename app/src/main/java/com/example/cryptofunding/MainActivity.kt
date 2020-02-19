package com.example.cryptofunding

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptofunding.contract.Contract
import com.example.cryptofunding.data.AppRoomDatabase
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.WalletHandler
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.WalletUtils
import java.math.BigInteger
import java.security.Provider
import java.security.Security


class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBouncyCastle()

        val repo = WalletRepository.getInstance(AppRoomDatabase.getInstance(this).walletDao())

        WalletHandler.generateNewWalletFileFromPrivateKey(
            "test",
            "myWallet",
            "7EB64DDB45D47DEF728B97D2B539B62CE7FCBF278D9610C96D8ED279A527FC96",
            filesDir.absolutePath,
            repo)

        val wallet = repo.getByName("myWallet")

        val credentials = WalletUtils.loadCredentials("test", wallet?.jsonPath)
        val contract = Contract.getInstance()
//        val result = contract.launchFundingProject(BigInteger.valueOf(1), mutableListOf(BigInteger.valueOf(10000000000000)), mutableListOf(BigInteger.valueOf(500)))
//        val receipt = result.sendAsync().join()
//        Log.d("CryptoDebug: ", receipt.gasUsed.toString())
        val result = contract.getDeadlineForCurrentTask(BigInteger.valueOf(0)).sendAsync().join()
        Log.d(DEBUG, result.toString())
    }

    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) { // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}
