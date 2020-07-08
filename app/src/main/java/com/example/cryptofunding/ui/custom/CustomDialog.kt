package com.example.cryptofunding.ui.custom

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Result
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.utils.WalletHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_key.*

class CustomDialog {
    fun showExportDialog(fragment: Fragment, wallet: Wallet) {
        val dialog = Dialog(fragment.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_key)

        dialog.exportButton.setOnClickListener {
            val result = WalletHandler.getPrivateKey(dialog.passwordEditText.text.toString(), wallet.jsonPath)

            result.observe(fragment.viewLifecycleOwner) {
                when (it.status) {
                    Result.Status.LOADING -> {
                        dialog.exportButton.isEnabled = false
                        dialog.exportButton.text = null
                        dialog.loadingAnimation.visibility = View.VISIBLE
                    }
                    Result.Status.SUCCESS -> {
                        dialog.exportTextView.visibility = View.GONE
                        dialog.passwordEditText.visibility = View.GONE
                        dialog.exportButton.visibility = View.GONE
                        dialog.loadingAnimation.visibility = View.GONE
                        dialog.warningTextView.visibility = View.VISIBLE
                        dialog.privateKeyFrame.visibility = View.VISIBLE
                        dialog.copyButton.visibility = View.VISIBLE
                        dialog.privateKeyTextView.text = it.data
                    }
                    Result.Status.ERROR -> {
                        dialog.exportButton.isEnabled = true
                        dialog.exportButton.text = fragment.getString(R.string.export)
                        dialog.loadingAnimation.visibility = View.GONE
                        dialog.passwordEditText.error = fragment.getString(R.string.incorrect_password)
                        dialog.passwordEditText.errorColor = ContextCompat.getColor(fragment.requireContext(), R.color.colorWarning)
                    }
                }
            }
        }

        dialog.copyButton.setOnClickListener {
            val clipboardManager: ClipboardManager = fragment.requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("pk", dialog.privateKeyTextView.text.toString())
            clipboardManager.primaryClip = clip

            Snackbar.make(fragment.requireView(), R.string.copied, Snackbar.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}