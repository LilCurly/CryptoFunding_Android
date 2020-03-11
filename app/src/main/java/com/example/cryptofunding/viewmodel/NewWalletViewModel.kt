package com.example.cryptofunding.viewmodel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.utils.DEBUG
import java.util.*
import javax.inject.Inject

@BindingAdapter("fadeVisible")
fun setFadeVisible(view: View, error: String?) {
    if (error == null && view.visibility == View.VISIBLE) {
        view.visibility = View.GONE
    }
    else if (error != null && view.visibility == View.GONE) {
        view.animate().cancel()
        view.visibility = View.VISIBLE
        view.alpha = 0F

        view.animate()
            .alpha(1F)
            .setDuration(150)
            .setInterpolator(LinearInterpolator())
            .setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.alpha = 1F
                }
            })
    }
}

class NewWalletViewModel @Inject constructor(private val repo: WalletRepository): ViewModel() {
    var name: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var privateKey: ObservableField<String> = ObservableField("")
    var nameError: ObservableField<String> = ObservableField()
    var passwordError: ObservableField<String> = ObservableField()
    var privateKeyError: ObservableField<String> = ObservableField()

    fun createWallet() {
        validateName()
        validePassword()

        if (nameError.get() == null && passwordError.get() == null) {
            
        }
    }

    fun importWallet() {
        validateName()
        validePassword()
        validatePrivateKey()

        if (nameError.get() == null && passwordError.get() == null && privateKeyError.get() == null) {
            Log.d(DEBUG, "Import ok")
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