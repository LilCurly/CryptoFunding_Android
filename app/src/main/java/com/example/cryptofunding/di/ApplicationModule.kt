package com.example.cryptofunding.di

import android.content.Context
import com.example.cryptofunding.data.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppRoomDatabase {
        return AppRoomDatabase.getInstance(context)
    }

    @JvmStatic
    @Reusable
    @Provides
    fun provideDao(database: AppRoomDatabase): AbstractWalletDao {
        return database.walletDao()
    }

    @JvmStatic
    @Reusable
    @Provides
    fun provideWalletRepository(dao: AbstractWalletDao): WalletRepository {
        return DefaultWalletRepository.getInstance(dao)
    }
}

@Module
abstract class ApplicationModuleBinds {

}