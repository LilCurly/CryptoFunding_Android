package com.example.cryptofunding.di

import android.content.Context
import com.example.cryptofunding.viewmodel.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class
])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    val walletListViewModel: WalletListViewModel
    val newWalletViewModel: NewWalletViewModel
    val homeViewModel: HomeViewModel
    val detailedProjectListViewModel: DetailedProjectListViewModel
    val projectsViewModel: ProjectsViewModel
    val addTaskViewModel: AddTaskViewModel
}