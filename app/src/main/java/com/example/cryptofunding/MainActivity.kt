package com.example.cryptofunding

import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptofunding.utils.WalletHandler
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wallet = WalletHandler.generateNewWalletFileFromPrivateKey("test", "7EB64DDB45D47DEF728B97D2B539B62CE7FCBF278D9610C96D8ED279A527FC96", filesDir.absolutePath)
        val credentials = WalletUtils.loadCredentials("test", wallet.pathToJSON)
        val web3j = Web3j.build(HttpService("https://ropsten.infura.io/v3/1e0e99dc4344427f903cedbb21639caa"))
        val contract = MyContract.load("0xefE02f786cEedcc4933f6f6C131e16099496A27e", web3j, credentials, DefaultGasProvider())
//        val result = contract.launchFundingProject(BigInteger.valueOf(1), mutableListOf(BigInteger.valueOf(10000000000000)), mutableListOf(BigInteger.valueOf(500)))
//        val receipt = result.sendAsync().join()
//        Log.d("CryptoDebug: ", receipt.gasUsed.toString())
        val result = contract.getDeadlineForCurrentTask(BigInteger.valueOf(0)).sendAsync().join()
        Log.d("CryptoDebug", result.toString())
    }
}
