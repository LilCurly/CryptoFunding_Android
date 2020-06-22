package com.example.cryptofunding


import android.animation.Animator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.data.Wallet
import com.example.cryptofunding.di.injector
import com.example.cryptofunding.ui.viewholder.WalletItem
import com.example.cryptofunding.utils.DEBUG
import com.example.cryptofunding.viewmodel.viewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.ISelectionListener
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.OnBindViewHolderListener
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.select.selectExtension
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_wallet_list.*
import kotlinx.android.synthetic.main.fragment_wallet_list.view.*
import kotlinx.android.synthetic.main.item_wallet.*

/**
 * A simple [Fragment] subclass.
 */
class WalletListFragment : Fragment() {
    lateinit var adapter: FastAdapter<WalletItem>
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
            if (it.isNotEmpty()) {
                wallet_list_nowallet.visibility = View.GONE
                wallet_list_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setupWalletList(it)
                handleClickListener()
                wallet_list_recyclerview.adapter = this.adapter
            }
            else {
                wallet_list_recyclerview.visibility = View.GONE
            }
        }

        viewModel.currentWallet.observe(viewLifecycleOwner) {
            showDetails()

            wallet_list_detailname.text = it.name
            wallet_list_notifamount.text = "0"
            it.amount.observe(viewLifecycleOwner) { currentAmount ->
                wallet_list_amount.text = currentAmount
            }
        }

        wallet_list_addwallet_fab.setOnClickListener {
            val action = WalletListFragmentDirections.actionWalletListFragmentToNewWalletFragment2()
            view.findNavController().navigate(action)
        }
    }

    private fun setupWalletList(wallets: List<Wallet>) {
        val itemAdapter = ItemAdapter<WalletItem>()
        adapter = FastAdapter.with(itemAdapter)
        adapter.selectExtension {
            isSelectable = true
        }

        itemAdapter.add(wallets.map {
            viewModel.loadAmountIfNeeded(it)
            val item = WalletItem(it)
            if (viewModel.isCurrentWallet(it)) {
                item.isSelected = true
            }
            item
        })
    }

    /**
     * Click listener for the items in the recyclerview
     */
    private fun handleClickListener() {
        adapter.onClickListener = { view, _, item, index ->
            if (!viewModel.isCurrentWallet(item.wallet)) {
                viewModel.setCurrentWallet(item.wallet)
                deselectRow()
                currentItemPosition = index
                launchScaleAnimation(view)
            }

            true
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
        activity?.toolbarTitle?.text = getString(R.string.my_wallets)
        activity?.walletImageView?.visibility = View.GONE
        activity?.closeImageView?.visibility = View.VISIBLE
        activity?.closeImageView?.setOnClickListener {
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
