package edu.devilsadvocate.tasktrackingactivity

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import edu.devilsadvocate.tasktrackingactivity.ItemTouchHelpers.ItemTouchHelperAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class TasksSection(
    @NonNull val title: String,
    @NonNull val tasks: MutableList<Task>,
    @NonNull val clickListener: ClickListener):
    Section(
        SectionParameters.builder()
            .itemResourceId(R.layout.recyclerview_item)
            .headerResourceId(R.layout.section_header)
            .build()
    ),
    ItemTouchHelperAdapter {

    override fun getContentItemsTotal() = tasks.size

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return TaskViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is TaskViewHolder) {
            val current: Task = tasks[position]
            holder.taskItemTitle.text = current.taskName
            holder.taskItemDescription.text = current.taskDescription
            holder.taskItemTime.text = current.taskTimeToCompletionInMinutes.toString() + " min"
            holder.taskRootView.setOnClickListener {
                clickListener.onItemRootViewClicked(title, holder.adapterPosition)
            }
            holder.title = this.title
            holder.id = current.id
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        if(holder is HeaderViewHolder){
            holder.headerTitle.text = title.substring(0,10)
        }
    }

    override fun onItemDismiss(sectionTitle: String, itemId: Int) : Boolean {
        if (this.title != sectionTitle)
            return false

        val indexOfItem: Int = tasks.indexOfFirst { itemId == it.id }

        if (indexOfItem == -1)
            return false

        clickListener.onTaskCompletion(tasks[indexOfItem])
        return true
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return false
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskItemTitle: TextView = itemView.findViewById(R.id.textTitle)
        val taskItemDescription: TextView = itemView.findViewById(R.id.textDescription)
        val taskItemTime: TextView = itemView.findViewById(R.id.textTime)
        val taskRootView: View = itemView.findViewById(R.id.item_root_view)
        var title: String? = null
        var id: Int? = null
    }

    inner class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
        val headerTitle: TextView = headerView.findViewById(R.id.headerTitle)
        val headerTime: TextView = headerView.findViewById(R.id.headerTime)
    }

    interface ClickListener {
        fun onItemRootViewClicked(
            sectionTitle: String,
            itemAdapterPosition: Int
        )

        fun onTaskCompletion(
            task: Task
        )
    }
}
