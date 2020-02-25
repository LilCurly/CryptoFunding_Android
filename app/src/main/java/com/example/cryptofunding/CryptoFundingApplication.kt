package com.example.cryptofunding

import android.app.Application
import com.example.cryptofunding.di.DaggerApplicationComponent
import com.example.cryptofunding.di.InjectorProvider

class CryptoFundingApplication: Application(), InjectorProvider {

    override val component by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}