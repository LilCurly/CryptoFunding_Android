package com.example.cryptofunding.ui.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptofunding.R
import com.example.cryptofunding.data.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(val context: Context, var tasksList: MutableList<Task>):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask = tasksList[position]
        holder.baseLayout.setOnClickListener {
            currentTask.expanded = !currentTask.expanded
            notifyItemChanged(position)
        }
        holder.setupHolder(currentTask)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val title = view.titleTextView
        private val day = view.dayTextView
        private val date = view.monthYearTextView
        private val amount = view.amountTextView
        private val summary = view.taskSummaryTextView
        val baseLayout = view.baseLayout
        private val expandableLayout = view.expandableLayout

        fun setupHolder(task: Task) {
            title.text = task.title
            amount.text = task.amount.toString()
            summary.text = task.summary
            day.text = task.limitDate.subSequence(0, 2)
            date.text = task.limitDate.subSequence(3, task.limitDate.count())

            if (task.expanded) expandableLayout.visibility = View.VISIBLE else expandableLayout.visibility = View.GONE
        }
    }
}