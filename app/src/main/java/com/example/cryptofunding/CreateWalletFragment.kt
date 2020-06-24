package com.example.cryptofunding

import android.graphics.Point
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.transition.addListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.databinding.FragmentCreateWalletBinding
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.getValueAnimator
import com.example.cryptofunding.viewmodel.NewWalletViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_new_wallet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class CreateWalletFragment : BaseNewWalletFragment() {
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

        setupEditTextDrawables()

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

        button_createwallet.setOnClickListener {
            subscribeUiForCoroutine(viewModel)
        }
    }

    private fun setupEditTextDrawables() {
        val density = resources.displayMetrics.density
        val size = (20 * density).roundToInt()

        val loginDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.login_drawable_selector)
        loginDrawable?.let {
            it.setBounds(0, 0, size, size)
            createwallet_login.setCompoundDrawables(it, null, null, null)
        }

        val passwordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.password_drawable_selector)
        passwordDrawable?.let {
            it.setBounds(0, 0, size, size)
            createwallet_password.setCompoundDrawables(it, null, null, null)
        }
    }

    private fun subscribeUiForCoroutine(viewModel: NewWalletViewModel) {
        context?.let {
            viewModel.createWallet(it.filesDir.absolutePath)?.observe(viewLifecycleOwner, Observer { result ->
                when (result.status) {
                    Result.Status.LOADING -> setButtonLoading()
                    Result.Status.SUCCESS -> {
                        setButtonSuccess()
                        Timer("Success", false).schedule(1000) {
                            (parentFragment as NewWalletFragment).popToWalletList()
                        }
                    }
                    Result.Status.ERROR -> {
                        setButtonFailure()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        Timer("Failure", false).schedule(1000) {
                            runBlocking {
                                withContext(Dispatchers.Main) {
                                    setButtonBase()
                                    enableNavigation()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setButtonLoading() {
        button_createwallet.isEnabled = false
        button_createwallet.text = null
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
        disableNavigation()
    }

    private fun setButtonSuccess() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
        successAnimation.visibility = View.VISIBLE
        successAnimation.playAnimation()
    }

    private fun setButtonFailure() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
        failureAnimation.visibility = View.VISIBLE
        failureAnimation.playAnimation()
    }

    private fun setButtonBase() {
        button_createwallet.isEnabled = true
        button_createwallet.text = resources.getString(R.string.create)
        successAnimation.visibility = View.GONE
        failureAnimation.visibility = View.GONE
    }
}
