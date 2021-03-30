package com.mahmoud.mvisample.presentation.mvi_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoud.mvisample.domain.mvi.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
abstract class BaseVM<ACTION : MVIAction, VIEW_STATE : MVIViewState, PARTIAL_STATE : MVIPartialState<VIEW_STATE>>() :
    ViewModel() {
    private val _partialState = MutableSharedFlow<PARTIAL_STATE>(extraBufferCapacity = 64)
    private val _intentFlow = MutableSharedFlow<ACTION>(extraBufferCapacity = 64)

    lateinit var viewState: StateFlow<VIEW_STATE>
    val partialState: Flow<PARTIAL_STATE> get() = _partialState

    protected abstract fun initialViewState(): VIEW_STATE
    protected abstract fun toPartialState(flow: Flow<ACTION>): Flow<PARTIAL_STATE>
    protected abstract fun canEmitForPartialStateForTypes(partialState: PARTIAL_STATE): Boolean


    suspend fun processIntent(intent: ACTION) = _intentFlow.emit(intent)

    init {
        initialProcess()
    }
    private fun initialProcess() {
        val initialVS = initialViewState()
        viewState = _intentFlow
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
            .toPartialStatFlow()
            .sendPartialEvent()
            .filter { it !is RecipeListPartialState.Error.ErrorLoadMore  }
            .scan(initialVS) { vs, partial -> partial.reduce(vs, initialVS) }
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)
    }

    private fun Flow<ACTION>.toPartialStatFlow(): Flow<PARTIAL_STATE> {
        return merge(
            toPartialState(this)
        )
    }

    private fun Flow<PARTIAL_STATE>.sendPartialEvent(): Flow<PARTIAL_STATE> {
        return onEach {
            if (canEmitForPartialStateForTypes(it)) {
                _partialState.emit(it)
            } else {
                return@onEach
            }
        }
    }
}