package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.model.Recipe

sealed class RecipeListPartialState : MVIPartialState<ViewState> {
    abstract override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState

    sealed class ListResult : RecipeListPartialState() {
        class ListResultInitial(private val recipeList: List<Recipe>,val isLast: Boolean) :
            RecipeListPartialState() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(list = recipeList, currentPage = oldVs.currentPage + 1,isLast = isLast)

            }
        }

        class ListResultLoadMore(private val recipeList: List<Recipe>, val isLast: Boolean) :
            RecipeListPartialState() {
            override fun reduce(oldVs: ViewState, initialVs: ViewState): ViewState {
                return initialVs.copy(list = oldVs.list + recipeList,
                    currentPage = oldVs.currentPage + 1, isLast = isLast)

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
                return initialVs.copy(loadingLoadMore = true,
                    list = oldVs.list,
                    currentPage = oldVs.currentPage)
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
                return initialVs.copy(errorLoadMore = throwable,
                    list = oldVs.list,
                    currentPage = oldVs.currentPage)
            }
        }
    }
}