package com.mahmoud.mvisample.domain.mvi

import androidx.paging.PagingData
import com.mahmoud.mvisample.domain.model.Recipe

sealed class RecipeListPartialState : MVIPartialState<ViewState> {

    class ListResult(private val recipeList: PagingData<Recipe>) : RecipeListPartialState() {
        override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
            return initialVs.copy(list = recipeList)
        }
    }

}