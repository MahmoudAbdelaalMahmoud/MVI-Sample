package com.mahmoud.mvisample.presentation.mvi_flow

import androidx.lifecycle.SavedStateHandle
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.mvi.RecipeListActions
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import com.mahmoud.mvisample.domain.mvi.ViewState
import com.mahmoud.mvisample.domain.usecase.GetRecipeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeListUseCase: GetRecipeListUseCase,
) : BaseVM<RecipeListActions, ViewState, RecipeListPartialState>() {

    init {
        initialProcess()
    }

    override fun initialViewState(): ViewState = ViewState()

    override fun toPartialState(flow: Flow<RecipeListActions>): Flow<RecipeListPartialState> {
        with(flow) {
            return merge(
                filterIsInstance<RecipeListActions.Initial>()
                    .flatMapLatest { getRecipeListUseCase(FIRST_PAGE) },
                filterIsInstance<RecipeListActions.Refresh>()
                    .flatMapLatest { getRecipeListUseCase(FIRST_PAGE) },
                filterIsInstance<RecipeListActions.LoadMore>()
                    .flatMapLatest { getRecipeListUseCase(it.page) }
            )
        }
    }

    override fun canEmitForPartialStateForTypes(partialState: RecipeListPartialState): Boolean =
        partialState is RecipeListPartialState.Error.ErrorInitial

    fun isLoadMoreDisabled(): Boolean {
        return viewState.value.isLoadMoreDisabled()
    }


}

