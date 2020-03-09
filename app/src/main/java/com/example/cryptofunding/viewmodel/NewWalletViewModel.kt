package com.example.cryptofunding.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.utils.DEBUG
import java.util.*
import javax.inject.Inject

enum class WalletCreationType {
    Import,
    Create
}

class NewWalletViewModel @Inject constructor(private val repo: WalletRepository): ViewModel() {
    var name: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var privateKey: ObservableField<String> = ObservableField("")
    var nameError: ObservableField<String> = ObservableField()
    var passwordError: ObservableField<String> = ObservableField()
    var privateKeyError: ObservableField<String> = ObservableField()

    var test: Int = 0

    fun saveWallet(type: WalletCreationType) {

        when (type) {
            WalletCreationType.Create -> {

            }
            WalletCreationType.Import -> {

            }
        }
    }

    fun validateName() {
        name.get()?.let {
            if (it.isBlank()) {
                nameError.set("Ce champ ne peut pas être vide")
            }
            else {
                nameError.set(null)
            }
        }
        //nameError.set("Ce champ ne peut pas être vide")
    }

    fun validePassword() {
        password.get()?.let {
            if (it.isBlank() || it.length < 8) {
                passwordError.set("Votre mot de passe doit contenir au moins 8 charactères")
            }
            else if (it.equals(it.toLowerCase(Locale.getDefault()))) {
                passwordError.set("Votre mot de passe doit contenir au moins une lettre capitale")
            }
            else if (!it.matches(Regex(".*\\d+.*"))) {
                passwordError.set("Votre mot de passe doit contenir au moins un charactère numérique")
            }
            else {
                passwordError.set(null)
            }
        }
    }

    fun validatePrivateKey() {
        privateKey.get()?.let {
            if (it.isBlank() || it.length != 64) {
                privateKeyError.set("Votre clé privée doit contenir 64 charactères")
            }
            else {
                privateKeyError.set(null)
            }
        }
    }
}