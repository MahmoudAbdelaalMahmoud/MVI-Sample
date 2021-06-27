package com.mahmoud.mvisample.domain.usecase

import androidx.paging.PagingData
import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.model.Recipe
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetRecipeListUseCase @Inject constructor(private val repository: IRepository) {
    suspend operator fun invoke(): Flow<PagingData<Recipe>> {
        return repository.getRecipeList()
    }
}