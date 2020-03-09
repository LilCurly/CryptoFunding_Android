package com.example.cryptofunding.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.utils.DEBUG
import javax.inject.Inject

enum class WalletCreationType {
    Import,
    Create
}

class NewWalletViewModel @Inject constructor(private val repo: WalletRepository): ViewModel() {
    var name: String? = null
    var password: String? = null
    var privateKey :String? = null

    var test: Int = 0

    fun saveWallet(type: WalletCreationType) {

        when (type) {
            WalletCreationType.Create -> {

            }
            WalletCreationType.Import -> {

            }
        }
    }

    fun test() {
        Log.d(DEBUG, test.toString())
        test++
    }
}