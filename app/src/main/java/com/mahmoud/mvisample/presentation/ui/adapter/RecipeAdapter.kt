package com.mahmoud.mvisample.presentation.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.domain.model.Recipe

class RecipeAdapter(private val actionHandler: IActionHandler) :
    PagingDataAdapter<Recipe, RecyclerView.ViewHolder>(Recipe_COMPARATOR) {
    companion object {
        private val Recipe_COMPARATOR = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.pk == newItem.pk
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeViewHolder initializeWith parent andClickHandler {
            actionHandler.openRecipe(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeViewHolder)
            getItem(position)?.let { holder.bind(it) }
    }
}


