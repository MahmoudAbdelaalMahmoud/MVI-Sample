package com.mahmoud.mvisample.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

abstract class DiffUtilCore<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {

    final override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    final override fun getOldListSize(): Int = oldList.size

    final override fun getNewListSize(): Int = newList.size

    final override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}

/**
 * Class DiffUtilBuilder to define DSL Kotlin to build [DiffUtilCore]
 */
class DiffUtilBuilder<T> {
    internal lateinit var adapter: RecyclerView.Adapter<*>
    internal lateinit var oldItems: MutableList<T>
    lateinit var newItems: List<T>
    lateinit var compareItems: (oldItem: T, newItem: T) -> Boolean

    /**
     * Function Used to propagate the change in adapter list
     * Use Coroutines for long running process [DiffUtil.calculateDiff(diff)]
     */
    fun propagate() = GlobalScope.launch(Dispatchers.Default) {
        if (oldItems.isEmpty()) {
            oldItems.addAll(newItems)
            withContext(Dispatchers.Main) { adapter.notifyDataSetChanged() }
            cancel()
        }
        val diff = object : DiffUtilCore<T>(oldItems, newItems) {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                if (!::compareItems.isInitialized) return false
                return compareItems(oldItem, newItem)
            }
        }
        val result = DiffUtil.calculateDiff(diff)
        oldItems.clear()
        oldItems.addAll(newItems)
        withContext(Dispatchers.Main) {
            result.dispatchUpdatesTo(adapter)
            cancel()
        }
    }
}