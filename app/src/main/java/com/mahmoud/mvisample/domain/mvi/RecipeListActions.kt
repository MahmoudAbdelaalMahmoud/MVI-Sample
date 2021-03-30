package com.mahmoud.mvisample.domain.mvi

sealed class RecipeListActions : MVIAction {
    object Initial : RecipeListActions()
    object Refresh : RecipeListActions()
    class LoadMore(val page: Int) : RecipeListActions()
}