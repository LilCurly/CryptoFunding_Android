package com.example.cryptofunding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.data.WalletRepository
import com.example.cryptofunding.ui.viewholder.WalletItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class WalletListViewModel @Inject  constructor(private val repo: WalletRepository) : ViewModel() {
    val wallets: LiveData<List<Wallet>> = repo.getAll()
    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    lateinit var adapter: FastAdapter<WalletItem>

    fun setupWalletList(walletList: List<Wallet>) {
        val itemAdapter = ItemAdapter<WalletItem>()
        adapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(walletList.map {
            WalletItem(it)
        })

        adapter.onClickListener = { _, _, item, _ ->
            setCurrentWallet(item.wallet)
            currentWallet.value = item.wallet
            true
        }
    }

    fun getCurrentWallet(): Wallet? {
        return repo.currentWallet
    }

    private fun setCurrentWallet(wallet: Wallet) {
        repo.currentWallet = wallet
    }
}