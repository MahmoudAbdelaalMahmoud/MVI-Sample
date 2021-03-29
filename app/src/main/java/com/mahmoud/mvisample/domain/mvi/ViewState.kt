package com.mahmoud.mvisample.domain.mvi

import com.mahmoud.mvisample.domain.model.Recipe

data class ViewState(
    val list: List<Recipe> = emptyList(),
    val loading: Boolean = false,
    val error: Throwable? = null,
    val errorLoadMore: Throwable? = null,
    val loadingLoadMore: Boolean = false,
    val empty: Boolean = false,
    val isLast: Boolean = false,
) : MVIViewState {
    fun isLoadMoreDisabled(): Boolean {
        return loading || loadingLoadMore || errorLoadMore != null || error != null || empty || isLast
    }
}