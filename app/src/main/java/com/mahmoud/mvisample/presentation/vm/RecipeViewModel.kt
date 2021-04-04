package com.mahmoud.mvisample.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
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

    private val initial by lazy {
        ObservableTransformer<RecipeListActions.Initial, RecipeListPartialState> { actions ->
            actions.flatMap {
                getRecipeListUseCase(FIRST_PAGE)
            }
        }
    }
    private val loadMore by lazy {
        ObservableTransformer<RecipeListActions.LoadMore, RecipeListPartialState> { actions ->
            actions.flatMap {
                getRecipeListUseCase(it.page)
            }
        }
    }
    private val refresh by lazy {
        ObservableTransformer<RecipeListActions.Refresh, RecipeListPartialState> { actions ->
            actions.flatMap {
                getRecipeListUseCase(FIRST_PAGE)
            }
        }
    }


    override fun handle(acrion: Observable<RecipeListActions>): List<Observable<out RecipeListPartialState>> =
        listOf(
            acrion.ofType(RecipeListActions.Initial::class.java)
                .take(1)
                .compose(initial),
            acrion.ofType(RecipeListActions.LoadMore::class.java)
                .compose(loadMore),
            acrion.ofType(RecipeListActions.Refresh::class.java)
                .compose(refresh),
        )

}

