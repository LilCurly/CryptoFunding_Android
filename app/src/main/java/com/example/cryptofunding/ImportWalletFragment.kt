package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.fragment_import_wallet.*

class ImportWalletFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (parentFragment as NewWalletFragment).viewModel
        button_importwallet.setOnClickListener {
            viewModel.test()
        }
    }
}
