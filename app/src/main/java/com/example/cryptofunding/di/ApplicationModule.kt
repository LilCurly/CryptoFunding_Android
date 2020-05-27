package com.example.cryptofunding.di

import android.content.Context
import com.example.cryptofunding.data.*
import com.example.cryptofunding.utils.INFURA_ADDRESS
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
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

    @JvmStatic
    @Reusable
    @Provides
    fun provideWeb3(): Web3j {
        return Web3j.build(HttpService(INFURA_ADDRESS))
    }
}

@Module
abstract class ApplicationModuleBinds {

}