package com.example.cryptofunding.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.cryptofunding.utils.INFURA_ADDRESS
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.RoundingMode

@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey @ColumnInfo(name = "id") val publicKey: String,
    val name: String,
    val jsonPath: String) {

    @Ignore
    private var amount = ""

    override fun equals(other: Any?): Boolean {
        val oWallet = other as Wallet
        return oWallet.publicKey == publicKey
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun getAmount(): String? {
        if (amount == "") {
            val web3j = Web3j.build(HttpService(INFURA_ADDRESS))
            val ethBalance =
                web3j.ethGetBalance(publicKey, DefaultBlockParameterName.LATEST).sendAsync().join()
            val balanceInEth = Convert.fromWei(ethBalance.balance.toString(), Convert.Unit.ETHER)
                .setScale(2, RoundingMode.HALF_EVEN)
            amount = balanceInEth.toString()
        }
        return amount
    }
}