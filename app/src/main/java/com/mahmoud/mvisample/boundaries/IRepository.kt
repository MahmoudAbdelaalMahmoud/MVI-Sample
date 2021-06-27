package com.mahmoud.mvisample.boundaries

import androidx.paging.PagingData
import com.mahmoud.mvisample.domain.model.Recipe
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getRecipeList(): Flow<PagingData<Recipe>>
}