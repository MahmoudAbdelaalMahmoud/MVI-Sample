package com.mahmoud.mvisample.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.R
import com.mahmoud.mvisample.databinding.RecipeItemLayout
import com.mahmoud.mvisample.domain.model.Recipe

class RecipeViewHolder(private val binding: RecipeItemLayout) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Recipe) {
        binding.item = item
    }

    companion object {

        private fun getItemLayoutId(): Int = R.layout.recipe_item_layout

        private fun inflateItemUsing(parent: ViewGroup): RecipeItemLayout =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                getItemLayoutId(),
                parent,
                false)

        infix fun initializeWith(parent: ViewGroup): RecipeViewHolder {
            return RecipeViewHolder(inflateItemUsing(parent))
        }
    }

    infix fun andClickHandler(handler: (item: Recipe) -> Unit): RecipeViewHolder {
        itemView.setOnClickListener {
            handler(binding.item!!)
        }
        return this
    }
}

class RecipeErrorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {

        private fun getItemLayoutId(): Int = R.layout.view_load_more_error

        private fun inflateErrorView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false)
        }

        infix fun initializeWith(parent: ViewGroup): RecipeErrorViewHolder {
            return RecipeErrorViewHolder(inflateErrorView(parent))
        }
    }
}

infix fun RecipeErrorViewHolder.andClickHandler(handler: () -> Unit): RecipeErrorViewHolder {
    itemView.setOnClickListener {
        handler()
    }
    return this
}

class RecipeLoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {

        private fun getItemLayoutId(): Int = R.layout.view_loading_more

        private fun inflateErrorView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false)
        }

        infix fun initializeWith(parent: ViewGroup): RecipeLoadingViewHolder {
            return RecipeLoadingViewHolder(inflateErrorView(parent))
        }
    }
}
