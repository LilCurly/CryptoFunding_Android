package com.example.cryptofunding


import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.custom.CustomDialog
import com.example.cryptofunding.ui.viewholder.WalletAdapter
import com.example.cryptofunding.viewmodel.viewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_wallet_list.*

/**
 * A simple [Fragment] subclass.
 */
class WalletListFragment : Fragment() {
    private lateinit var oAdapter: RecyclerSwipeAdapter<WalletAdapter.ViewHolder>
    private var currentItemPosition: Int? = null

    private val viewModel by viewModel {
        requireActivity().injector.walletListViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        hideDetails()

        wallet_list_recyclerview.itemAnimator = null

        viewModel.wallets.observe(viewLifecycleOwner) {
            if (wallet_list_recyclerview.adapter == null) {
                if (it.isNotEmpty()) {
                    wallet_list_nowallet.visibility = View.GONE
                    wallet_list_recyclerview.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    setupWalletList(it)
                    wallet_list_recyclerview.adapter = oAdapter
                } else {
                    wallet_list_recyclerview.visibility = View.GONE
                }
            }
        }

        viewModel.currentWallet.observe(viewLifecycleOwner) {
            if (it != null) {
                showDetails()

                wallet_list_detailname.text = it.name
                wallet_list_notifamount.text = "0"
                it.amount.observe(viewLifecycleOwner) { currentAmount ->
                    wallet_list_amount.text = currentAmount
                }
            } else {
                hideDetails()
            }
        }

        wallet_list_addwallet_fab.setOnClickListener {
            val action = WalletListFragmentDirections.actionWalletListFragmentToNewWalletFragment2()
            view.findNavController().navigate(action)
        }
    }

    private fun setupWalletList(wallets: List<Wallet>) {
        oAdapter = WalletAdapter(requireContext(), wallets.mapIndexed { index, wallet ->
            viewModel.loadAmountIfNeeded(wallet)
            if (viewModel.isCurrentWallet(wallet)) {
                currentItemPosition = index
                viewModel.currentWallet.postValue(wallet)
            }
            wallet
        }.toMutableList(), { view, _, item, position ->
            if (!viewModel.isCurrentWallet(item)) {
                viewModel.setCurrentWallet(item)
                deselectRow()
                currentItemPosition = position
                launchScaleAnimation(view)
            }
        }, { item, position ->
            viewModel.wallets.value?.let {
                (oAdapter as WalletAdapter).walletList.removeAt(position)
                wallet_list_recyclerview.removeViewAt(position)
                oAdapter.notifyItemRemoved(position)
                oAdapter.notifyItemRangeChanged(position, it.size - 1)
                currentItemPosition?.let { itemPosition ->
                    if (position == itemPosition) {
                        currentItemPosition = null
                        (oAdapter as WalletAdapter).clickedPosition = -1
                        viewModel.setCurrentWallet(null)
                    } else if (position < itemPosition) {
                        currentItemPosition!!.dec()
                        (oAdapter as WalletAdapter).clickedPosition = currentItemPosition!!
                    }
                }
                viewModel.deleteWallet(item)

                Snackbar.make(requireView(), R.string.wallet_deleted, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.cancel) { _ ->
                        (oAdapter as WalletAdapter).walletList.add(position, item)
                        oAdapter.notifyItemInserted(position)
                        oAdapter.notifyItemRangeChanged(position, it.size - 1)
                        viewModel.addWallet(item)
                    }.show()
            }
        }, {
            CustomDialog().showExportDialog(this, it)
        })
        currentItemPosition?.let {
            (oAdapter as WalletAdapter).clickedPosition = it
        }
    }

    /**
     * Showing the details of the currently selected wallet
     */
    private fun showDetails() {
        wallet_list_noselected.visibility = View.GONE
        wallet_list_detailname.visibility = View.VISIBLE
        wallet_list_detailname.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        wallet_list_amount.visibility = View.VISIBLE
        wallet_list_amount.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        wallet_list_available.visibility = View.VISIBLE
        wallet_list_currency.visibility = View.VISIBLE
        wallet_list_notification.visibility = View.VISIBLE
        wallet_list_notifamount.visibility = View.VISIBLE
        wallet_list_notifamount.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }

    /**
     * Hiding the details if no waller is selected
     */
    private fun hideDetails() {
        wallet_list_noselected.visibility = View.VISIBLE
        wallet_list_detailname.visibility = View.GONE
        wallet_list_amount.visibility = View.GONE
        wallet_list_available.visibility = View.GONE
        wallet_list_currency.visibility = View.GONE
        wallet_list_notification.visibility = View.GONE
        wallet_list_notifamount.visibility = View.GONE
    }

    private fun setupToolbar() {
        requireActivity().toolbarTitle.text = getString(R.string.my_wallets)
        requireActivity().walletFrameLayout.visibility = View.GONE
        requireActivity().addFrameLayout.visibility = View.GONE
        requireActivity().closeFrameLayout.visibility = View.VISIBLE
        requireActivity().closeFrameLayout.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Animation launched when the item currently selected gets deselected
     */
    private fun deselectRow() {
        currentItemPosition?.let { position ->
            // Using the position in the recyclerview to retrieve the item with the LayoutManager if it is on the screen.
            wallet_list_recyclerview.layoutManager?.findViewByPosition(position)?.let {
                it.animation = null
                it.animate()
                    .setDuration(50)
                    .scaleY(1f)
                    .scaleX(1f)
                    .setStartDelay(10)
                    .setInterpolator(LinearInterpolator())
                    .setListener(null)
                    .start()
            }
        }
    }

    /**
     * Animation launched when an item in the recyclerview is selected
     * Arg: The view representing the selected item
     */
    private fun launchScaleAnimation(view: View?) {
        view?.let {
            val interpolator = LinearInterpolator()
            it.animate()
                .setDuration(50)
                .scaleX(1.06f)
                .scaleY(1.06f)
                .setStartDelay(10)
                .setInterpolator(interpolator)
                .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        it.animate()
                            .setDuration(100)
                            .scaleX(1.04f)
                            .scaleY(1.04f)
                            .setInterpolator(interpolator)
                            .start()
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }

                })
                .start()
        }
    }
}
