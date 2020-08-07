package com.example.cryptofunding.ui.viewholder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.daimajia.swipe.util.Attributes
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Task
import com.example.cryptofunding.ui.custom.ClippingConstraintLayout
import com.example.cryptofunding.utils.DEBUG
import kotlinx.android.synthetic.main.item_new_task.view.*

class NewTaskAdapter(val context: Context,
                     var tasksList: MutableList<Task>):
    RecyclerSwipeAdapter<NewTaskAdapter.ViewHolder>() {

    init {
        mItemManger.mode = Attributes.Mode.Single
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_task, parent, false)
        return ViewHolder(view)
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.taskSwipeLayout
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = tasksList[position]
        mItemManger.bindView(viewHolder.swipeLayout, position)

        viewHolder.amount.text = task.amount.toString()
        viewHolder.title.text = task.title
        viewHolder.day.text = task.limitDate.subSequence(0, 2)
        viewHolder.monthYear.text = task.limitDate.subSequence(3, 8)
        viewHolder.explanation.text = task.summary

        viewHolder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

        if (mItemManger.isOpen(position)) {
            viewHolder.swipeLayout.open(false)
        }

        if (!task.expanded) {
            viewHolder.expandableLayout.visibility = View.GONE
        }

        if (task.expanded) {
            viewHolder.swipeLayout.isRightSwipeEnabled = false
        }

        setupSwipeListener(viewHolder, position, task)
        setupClickListener(viewHolder, position, task)
    }

    private fun setupSwipeListener(
        viewHolder: ViewHolder,
        position: Int,
        task: Task
    ) {
        viewHolder.swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onOpen(layout: SwipeLayout?) {

            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {

            }

            override fun onStartOpen(layout: SwipeLayout?) {
                viewHolder.swipeLayout.setOnClickListener(null)
            }

            override fun onStartClose(layout: SwipeLayout?) {
                viewHolder.swipeLayout.setOnClickListener(null)
            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {

            }

            override fun onClose(layout: SwipeLayout?) {
                setupClickListener(viewHolder, position, task)
            }

        })
    }

    private fun setupClickListener(
        viewHolder: ViewHolder,
        position: Int,
        task: Task
    ) {
        viewHolder.swipeLayout.setOnClickListener {
            if (!mItemManger.isOpen(position)) {
                if (task.expanded) {
                    viewHolder.expandableLayout.visibility = View.GONE
                    viewHolder.swipeLayout.isRightSwipeEnabled = true
                    task.expanded = false
                } else {
                    viewHolder.expandableLayout.visibility = View.VISIBLE
                    viewHolder.swipeLayout.isRightSwipeEnabled = false
                    task.expanded = true
                }
            }
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val baseLayout: ClippingConstraintLayout = view.baseLayout
        val swipeLayout: SwipeLayout = view.taskSwipeLayout
        val expandableLayout: ConstraintLayout = view.expandableLayout
        val title: TextView = view.titleTextView
        val day: TextView = view.dayTextView
        val monthYear: TextView = view.monthYearTextView
        val amount: TextView = view.amountTextView
        val explanation: TextView = view.taskSummaryTextView

        val deleteButton: FrameLayout = view.deleteButton
        val editButton: FrameLayout = view.editButton
    }
}