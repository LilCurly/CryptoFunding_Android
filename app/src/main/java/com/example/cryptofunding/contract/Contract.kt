package com.example.cryptofunding.contract

import com.example.cryptofunding.utils.CONTRACT_ADDRESS
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider

class Contract {
    companion object {
        @Volatile private var instance: MyContract? = null

        fun getInstance(): MyContract {
            return instance ?: synchronized(this) {
                instance ?: MyContract.load(
                    CONTRACT_ADDRESS,
                    Web3j.build(HttpService("https://ropsten.infura.io/v3/1e0e99dc4344427f903cedbb21639caa")),
                    Credentials.create(Keys.createEcKeyPair()),
                    DefaultGasProvider())
            }
        }

        fun reloadInstance(credentials: Credentials): MyContract {
            return MyContract.load(
                CONTRACT_ADDRESS,
                Web3j.build(HttpService("https://ropsten.infura.io/v3/1e0e99dc4344427f903cedbb21639caa")),
                credentials,
                DefaultGasProvider()).also { instance = it }
        }
    }
}