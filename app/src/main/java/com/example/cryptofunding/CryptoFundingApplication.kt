package com.example.cryptofunding

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.cryptofunding.di.DaggerApplicationComponent
import com.example.cryptofunding.di.InjectorProvider

class CryptoFundingApplication: Application(), ViewModelStoreOwner, InjectorProvider {

    override val component by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}