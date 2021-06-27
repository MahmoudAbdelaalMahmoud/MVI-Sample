package com.mahmoud.mvisample.presentation.ui

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.domain.mvi.ViewState
import com.mahmoud.mvisample.presentation.ui.adapter.RecipeAdapter


@BindingAdapter( "viewState", requireAll = false)
fun RecyclerView.setRecipesState(viewState: ViewState?) {
    viewState?.let {
        adapter?.let {
            ((it as ConcatAdapter).adapters[1] as RecipeAdapter).apply {
                viewState.list?.let { recipes ->
                    findViewTreeLifecycleOwner()?.lifecycle?.let { lifeCycle ->
                        this.submitData(lifeCycle, recipes)
                    }
                }
            }
        }
    }
}

