package com.mahmoud.mvisample.boundaries

import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import io.reactivex.rxjava3.core.Single

interface IRepository {
     fun getRecipeList(page: Int): Single<RecipeListPartialState>
}