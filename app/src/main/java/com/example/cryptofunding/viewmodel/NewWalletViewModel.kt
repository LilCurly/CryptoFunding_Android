package com.example.cryptofunding.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.WalletRepository
import javax.inject.Inject

enum class WalletCreationType {
    Import,
    Create
}

class NewWalletViewModel @Inject constructor(private val repo: WalletRepository): ViewModel() {
    var name: String? = null
    var password: String? = null
    var privateKey :String? = null

    fun saveWallet(type: WalletCreationType) {

        when (type) {
            WalletCreationType.Create -> {

            }
            WalletCreationType.Import -> {

            }
        }
    }
}