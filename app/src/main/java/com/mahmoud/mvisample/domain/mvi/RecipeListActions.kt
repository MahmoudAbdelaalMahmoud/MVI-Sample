package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE

sealed class RecipeListActions(open val page: Int) : MVIAction {
    class Initial(override val page: Int = FIRST_PAGE) : RecipeListActions(page)
    class Refresh(override val page: Int = FIRST_PAGE) : RecipeListActions(page)
    class LoadMore(override val page: Int) : RecipeListActions(page)
}