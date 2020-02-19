package com.example.cryptofunding.data

class FakeWalletDao: AbstractWalletDao {
    private val wallets = mutableListOf<Wallet>()

    override fun getAll(): List<Wallet> {
        return wallets.toList()
    }

    override fun getByPublicKey(publicKey: String): Wallet? {
        return wallets.first {
            it.publicKey == publicKey
        }
    }

    override fun getByName(name: String): Wallet? {
        return wallets.first {
            it.name == name
        }
    }

    override fun insertWallet(wallet: Wallet) {
        wallets.add(wallet)
    }

    override fun deleteWallet(wallet: Wallet) {
        wallets.remove(wallet)
    }

    override fun exists(publicKey: String): Wallet? {
        return wallets.first { it.publicKey == publicKey}
    }
}