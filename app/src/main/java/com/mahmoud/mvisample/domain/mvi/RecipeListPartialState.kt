package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.model.Recipe

sealed class RecipeListPartialState :MVIPartialState<ViewState>{
    abstract override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState
    sealed class ListResult:RecipeListPartialState() {
        class ListResultInitial(private val recipeList: List<Recipe>, isLast: Boolean) : RecipeListPartialState() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(list = recipeList)

            }
        }
        class ListResultLoadMore(private val recipeList: List<Recipe>, isLast: Boolean) : RecipeListPartialState() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(list = oldVs.list + recipeList)

            }
        }
    }


    object Empty : RecipeListPartialState() {
        override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
            return initialVs.copy(empty = true)
        }
    }

    sealed class Loading : RecipeListPartialState() {
        object InitialLoading : Loading() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(loading = true)
            }
        }

        object LoadMoreLoading : Loading() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(loadingLoadMore = true, list = oldVs.list)
            }
        }
    }

    sealed class Error : RecipeListPartialState() {
        class ErrorInitial(private val throwable: Throwable) : Error() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(error = throwable)
            }
        }

        class ErrorLoadMore(private val throwable: Throwable) : Error() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(errorLoadMore = throwable, list = oldVs.list)
            }
        }
    }
}