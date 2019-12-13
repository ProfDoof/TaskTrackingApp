package edu.devilsadvocate.tasktrackingactivity.ItemTouchHelpers

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import edu.devilsadvocate.tasktrackingactivity.TasksSection

class SimpleItemTouchHelperCallback(val adapters: List<ItemTouchHelperAdapter>): ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        val swipeFlags = (ItemTouchHelper.START or ItemTouchHelper.END)
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is TasksSection.TaskViewHolder) {
            for (adapter in adapters) {
                if (adapter.onItemDismiss(viewHolder.title ?: "", viewHolder.id ?: -1))
                    break
            }
        }
    }
}