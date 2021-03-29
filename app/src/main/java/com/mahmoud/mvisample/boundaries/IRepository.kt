package com.mahmoud.mvisample.boundaries

import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState

interface IRepository {
    suspend fun getRecipeList(page: Int): RecipeListPartialState
}