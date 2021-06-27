package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE

sealed class RecipeListActions : MVIAction {
    object Initial : RecipeListActions()
}