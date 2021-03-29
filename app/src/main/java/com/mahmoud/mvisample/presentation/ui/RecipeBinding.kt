package com.mahmoud.mvisample.presentation.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.domain.mvi.ViewState
import com.mahmoud.mvisample.presentation.ui.adapter.ItemListStatus
import com.mahmoud.mvisample.presentation.ui.adapter.RecipeAdapter


@BindingAdapter("viewState", requireAll = false)
fun RecyclerView.setRecipesState(oldState: ViewState?, viewState: ViewState?) {
    if (oldState == viewState) return
    viewState?.let {
        handleAdapterStates(it)
    }
}


private fun RecyclerView.handleAdapterStates(viewState: ViewState) {
    when {
        viewState.errorLoadMore != null -> {
            (adapter as RecipeAdapter).status = ItemListStatus.Error
        }
        viewState.loadingLoadMore -> {
            (adapter as RecipeAdapter).status = ItemListStatus.Loading
        }
        else -> {
            if (viewState.list.isNotEmpty())
                setRecipesToAdapter(viewState)
        }
    }
}


private fun RecyclerView.setRecipesToAdapter(viewState: ViewState) {
    adapter?.let {
        (it as RecipeAdapter).apply {
            if (viewState.list != this.recipes) {
                this.setData(viewState.list)
            }
        }

    }
}
