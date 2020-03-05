package com.example.cryptofunding.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.INFURA_ADDRESS
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.RoundingMode

@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey @ColumnInfo(name = "id") val publicKey: String,
    val name: String,
    val jsonPath: String) {

    @Ignore
    var amount = MutableLiveData<String>()

    override fun equals(other: Any?): Boolean {
        val oWallet = other as Wallet
        return oWallet.publicKey == publicKey
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun loadAmount() {
        val web3j = Web3j.build(HttpService(INFURA_ADDRESS))
        web3j.ethGetBalance(publicKey, DefaultBlockParameterName.LATEST).sendAsync().whenComplete { result, _ ->
            val balanceInEth = Convert.fromWei(result.balance.toString(), Convert.Unit.ETHER)
                .setScale(2, RoundingMode.HALF_EVEN)
            amount.postValue(balanceInEth.toString())
        }
    }
}