package com.example.cryptofunding

import android.app.Application
import dagger.Component

@Component
interface ApplicationComponent {

}

class App: Application() {
    val appComponent = DaggerApplicationComponent.create()
}