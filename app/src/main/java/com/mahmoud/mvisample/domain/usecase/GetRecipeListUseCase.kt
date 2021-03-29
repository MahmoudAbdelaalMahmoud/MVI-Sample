package com.mahmoud.mvisample.domain.usecase

import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetRecipeListUseCase @Inject constructor(private val repository: IRepository) {
    suspend operator fun invoke(page: Int): Flow<RecipeListPartialState> {
        val isFirstPage = page == FIRST_PAGE
        return flow {
            emit(repository.getRecipeList(page))
        }
            .onStart { emit(getLoadingAccordingPage(isFirstPage)) }
            .catch {
                emit(getErrorAccordingPage(isFirstPage, it))
            }
    }

    private fun getLoadingAccordingPage(isFirstPage: Boolean): RecipeListPartialState.Loading {
        return if (isFirstPage) RecipeListPartialState.Loading.InitialLoading
        else RecipeListPartialState.Loading.LoadMoreLoading
    }

    private fun getErrorAccordingPage(
        isFirstPage: Boolean,
        throwable: Throwable,
    ): RecipeListPartialState.Error {
        return if (isFirstPage) RecipeListPartialState.Error.ErrorInitial(throwable)
        else RecipeListPartialState.Error.ErrorLoadMore(throwable)
    }
}