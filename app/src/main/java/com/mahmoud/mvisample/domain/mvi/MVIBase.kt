package com.mahmoud.mvisample.domain.mvi

interface MVIAction
interface MVIViewState
interface MVIPartialState<S:MVIViewState>{
    fun reduce(oldVs:S,initialVs:S):S
}