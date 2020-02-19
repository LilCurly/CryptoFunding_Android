package com.example.cryptofunding

import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptofunding.contract.Contract
import com.example.cryptofunding.data.AppRoomDatabase
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.utils.CONTRACT_ADDRESS
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.WalletHandler
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repo = WalletRepository.getInstance(AppRoomDatabase.getInstance(this).walletDao())

        WalletHandler.generateNewWalletFileFromPrivateKey(
            "test",
            "myWallet",
            "7EB64DDB45D47DEF728B97D2B539B62CE7FCBF278D9610C96D8ED279A527FC96",
            filesDir.absolutePath,
            repo)

        val wallet = repo.getByName("myWallet")

        val credentials = WalletUtils.loadCredentials("test", wallet?.jsonPath)
        val contract = Contract.reloadInstance(credentials)
//        val result = contract.launchFundingProject(BigInteger.valueOf(1), mutableListOf(BigInteger.valueOf(10000000000000)), mutableListOf(BigInteger.valueOf(500)))
//        val receipt = result.sendAsync().join()
//        Log.d("CryptoDebug: ", receipt.gasUsed.toString())
        val result = contract.getDeadlineForCurrentTask(BigInteger.valueOf(0)).sendAsync().join()
        Log.d(DEBUG, result.toString())
    }
}
