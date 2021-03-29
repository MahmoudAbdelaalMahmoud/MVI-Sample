package com.mahmoud.mvisample.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.mahmoud.mvisample.domain.mvi.RecipeListActions
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import com.mahmoud.mvisample.domain.mvi.ViewState
import com.mahmoud.mvisample.domain.usecase.GetRecipeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecipeListUseCase: GetRecipeListUseCase,
) : BaseVM<RecipeListActions, ViewState, RecipeListPartialState>() {

    override val initialState by lazy { ViewState() }

    override fun reduce(result: RecipeListPartialState, previousState: ViewState): ViewState {
        return result.reduce(previousState, initialState)
    }

    private val getRecipeList by lazy {
        ObservableTransformer<RecipeListActions, RecipeListPartialState> { actions ->
            actions.flatMap {
                getRecipeListUseCase(page = it.page)
            }
        }
    }


    override fun handle(shared: Observable<RecipeListActions>): List<Observable<out RecipeListPartialState>> =
        listOf(
            shared.ofType(RecipeListActions::class.java)
                .compose(getRecipeList)
        )

}

