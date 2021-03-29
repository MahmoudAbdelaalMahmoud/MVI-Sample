package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE

sealed class RecipeListActions : MVIAction {
    class Initial(val page: Int = FIRST_PAGE) : RecipeListActions()
    class Refresh(val page: Int = FIRST_PAGE) : RecipeListActions()
    class LoadMore(val page: Int) : RecipeListActions()
}