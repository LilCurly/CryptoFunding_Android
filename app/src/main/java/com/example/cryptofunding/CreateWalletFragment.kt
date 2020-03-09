package com.example.cryptofunding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cryptofunding.databinding.FragmentCreateWalletBinding
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.fragment_create_wallet.*

/**
 * A simple [Fragment] subclass.
 */
class CreateWalletFragment : Fragment() {
    lateinit var binding: FragmentCreateWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_wallet, container, false)
        binding.viewModel = (parentFragment as NewWalletFragment).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (parentFragment as NewWalletFragment).viewModel

        binding.createwalletLogin.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateName()
            }
        }

        binding.createwalletPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validePassword()
            }
        }
    }

}
