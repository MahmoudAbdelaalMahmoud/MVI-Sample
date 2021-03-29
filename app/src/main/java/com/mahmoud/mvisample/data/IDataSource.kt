package com.mahmoud.mvisample.data

import com.mahmoud.mvisample.data.remote.RecipeDto

interface IDataSource {
    suspend fun getRecipeBy(id: Int): RecipeDto
    suspend fun getRecipeList(page: Int): List<RecipeDto>
}