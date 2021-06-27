package com.mahmoud.mvisample.presentation.mvi_flow

import androidx.lifecycle.SavedStateHandle
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


    override fun initialViewState(): ViewState = ViewState()

    override fun toPartialState(flow: Flow<RecipeListActions>): Flow<RecipeListPartialState> {
        with(flow) {
            return merge(
                flatMapLatest {
                    getRecipeListUseCase().map { RecipeListPartialState.ListResult(it) }
                }
            )
        }
    }

    override fun canEmitPartialStateForTypes(partialState: RecipeListPartialState): Boolean =
        false



    override fun stopReducePartialStateForTypes(partialState: RecipeListPartialState): Boolean {
        return false
    }


}

