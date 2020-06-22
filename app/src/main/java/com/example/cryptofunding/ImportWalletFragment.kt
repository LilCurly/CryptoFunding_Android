package com.example.cryptofunding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.databinding.FragmentCreateWalletBinding
import com.example.cryptofunding.databinding.FragmentImportWalletBinding
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.NewWalletViewModel
import com.example.cryptofunding.viewmodel.viewModel
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_import_wallet.*
import kotlinx.android.synthetic.main.fragment_new_wallet_animated.*
import kotlin.math.roundToInt

class ImportWalletFragment : Fragment() {
    private lateinit var binding: FragmentImportWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_import_wallet, container, false)
        binding.viewModel = (parentFragment as NewWalletFragment).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (parentFragment as NewWalletFragment).viewModel

        setupEditTextDrawables()

        binding.importwalletLogin.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateName()
            }
        }

        binding.importwalletPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validePassword()
            }
        }

        binding.importwalletPrivatekey.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validatePrivateKey()
            }
        }

        button_importwallet.setOnClickListener {
            subscribeUiForCoroutine(viewModel)
        }
    }

    private fun setupEditTextDrawables() {
        val density = resources.displayMetrics.density
        val size = (20 * density).roundToInt()

        val loginDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.login_drawable_selector)
        loginDrawable?.let {
            it.setBounds(0, 0, size, size)
            importwallet_login.setCompoundDrawables(it, null, null, null)
        }

        val passwordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.password_drawable_selector)
        passwordDrawable?.let {
            it.setBounds(0, 0, size, size)
            importwallet_password.setCompoundDrawables(it, null, null, null)
        }

        val keyDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.key_drawable_selector)
        keyDrawable?.let {
            it.setBounds(0, 0, size, size)
            importwallet_privatekey.setCompoundDrawables(it, null, null, null)
        }
    }

    private fun subscribeUiForCoroutine(viewModel: NewWalletViewModel) {
        context?.let {
            viewModel.createWallet(it.filesDir.absolutePath)?.observe(viewLifecycleOwner, Observer { result ->
                when (result.status) {
                    Result.Status.LOADING -> Log.d(DEBUG, "Loading")
                    Result.Status.SUCCESS -> Log.d(DEBUG, "Success")
                    Result.Status.ERROR -> Log.d(DEBUG, "Error")
                }
            })
        }
    }
}
