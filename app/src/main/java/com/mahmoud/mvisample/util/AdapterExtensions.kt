package com.mahmoud.mvisample.util

import androidx.recyclerview.widget.RecyclerView


fun <T> RecyclerView.Adapter<*>.propagateTo(
    oldList: MutableList<T>,
    block: DiffUtilBuilder<T>.() -> Unit,
) {
    DiffUtilBuilder<T>().also {
        it.adapter = this
        it.oldItems = oldList
        block(it)
    }.propagate()
}