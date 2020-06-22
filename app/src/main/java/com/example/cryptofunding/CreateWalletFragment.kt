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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.transition.addListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.databinding.FragmentCreateWalletBinding
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.getValueAnimator
import com.example.cryptofunding.viewmodel.NewWalletViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_new_wallet.*
import kotlin.math.roundToInt

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
                    Result.Status.LOADING -> animateView()
                    Result.Status.SUCCESS -> Log.d(DEBUG, "Success")
                    Result.Status.ERROR -> Log.d(DEBUG, "Error")
                }
            })
        }
    }

    private fun animateView() {
        val parentFragment = (parentFragment as NewWalletFragment)
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        val loginOrigin = createwallet_login.x
        val passwordOrigin = createwallet_password.x
        val tabOrigin = parentFragment.newWallet_tablayout.y
        val buttonHeightOrigin = button_createwallet.height
        val buttonHeightDest = ((requireView().height / 2) + 250) - (buttonHeightOrigin / 2)
        val buttonYOrigin = button_createwallet.y
        val buttonYDest = ((size.y / 2) - 250) + (buttonHeightDest / 2)
        display.getSize(size)
        val animator = getValueAnimator(true, 700, AccelerateDecelerateInterpolator()) { progress ->
            createwallet_login.x = (loginOrigin - (size.x - loginOrigin) * progress)
            createwallet_password.x = (passwordOrigin + (size.x - passwordOrigin) * progress)
            parentFragment.newWallet_tablayout.y = (tabOrigin - (size.y - tabOrigin) * progress)
            button_createwallet.y = (buttonYOrigin + (buttonYDest - buttonYOrigin) * (progress * progress * progress))
            val newHeight = ((buttonHeightOrigin + (buttonHeightDest - buttonHeightOrigin) * progress)).toInt()
            val params = button_createwallet.layoutParams as ConstraintLayout.LayoutParams
            params.height = newHeight
            button_createwallet.layoutParams = params
            requireActivity().toolbar.alpha = (1 + (0 - 1) * progress)
        }

        animator.doOnStart {
            parentFragment.newWallet_pager.isUserInputEnabled = false
            button_createwallet.setOnClickListener(null)
        }

        animator.doOnEnd {
            requireActivity().toolbar.visibility = View.GONE
        }

        animator.start()
    }

}
