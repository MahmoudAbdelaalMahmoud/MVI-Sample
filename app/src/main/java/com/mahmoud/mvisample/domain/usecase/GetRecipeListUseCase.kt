package com.mahmoud.mvisample.domain.usecase

import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetRecipeListUseCase @Inject constructor(private val repository: IRepository) {
    operator  fun invoke(page: Int): Observable<RecipeListPartialState> {
        val isFirstPage = page == FIRST_PAGE
        return repository.getRecipeList(page)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .startWithItem(getLoadingAccordingPage(isFirstPage))
            .onErrorReturn { getErrorAccordingPage(isFirstPage, it) }

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