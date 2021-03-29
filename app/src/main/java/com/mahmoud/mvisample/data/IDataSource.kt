package com.mahmoud.mvisample.data

import com.mahmoud.mvisample.data.remote.RecipeDto
import io.reactivex.rxjava3.core.Single

interface IDataSource {
    fun getRecipeBy(id: Int): Single<RecipeDto>
    fun getRecipeList(page: Int): Single<List<RecipeDto>>
}