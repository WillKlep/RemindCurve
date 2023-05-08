package com.willklep.remindcurve

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.willklep.remindcurve.R
import com.willklep.remindcurve.Task

class TaskAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(recycler_item: View) : RecyclerView.ViewHolder(recycler_item) {
        val topicTextView: TextView = recycler_item.findViewById(R.id.topic_textview)
        val classTextView: TextView = recycler_item.findViewById(R.id.class_textview)
        val dateRangeTextView: TextView = recycler_item.findViewById(R.id.start_date_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.topicTextView.text = currentTask.topic
        holder.classTextView.text = currentTask.className
        val dateRangeText = "${currentTask.startDate} - ${currentTask.endDate}"
        holder.dateRangeTextView.text = dateRangeText
    }

    override fun getItemCount() = tasks.size
}
