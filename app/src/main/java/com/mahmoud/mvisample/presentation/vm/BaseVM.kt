package com.mahmoud.mvisample.presentation.vm

import com.mahmoud.mvisample.domain.mvi.MVIAction
import com.mahmoud.mvisample.domain.mvi.MVIPartialState
import com.mahmoud.mvisample.domain.mvi.MVIViewState
import androidx.lifecycle.ViewModel
import com.mahmoud.mvisample.domain.mvi.RecipeListActions
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.merge


abstract class BaseVM<A : MVIAction, S : MVIViewState, R : MVIPartialState<S>> : ViewModel() {

    val partialStatPublisher: PublishSubject<R> = PublishSubject.create()

    internal val intentsSubject: PublishSubject<A> = PublishSubject.create()
    private val statesObservable: Observable<S> by lazy { compose() }
    private val disposables by lazy { CompositeDisposable() }

    abstract val initialState: S
    abstract fun reduce(result: R, previousState: S): S
    abstract fun handle(action: Observable<A>): List<Observable<out R>>



    fun processIntents(intents: Observable<A>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    fun states(): Observable<S> = statesObservable
    private val actionProcessor by lazy {
        ObservableTransformer<A, R> { actions ->
            actions.publish { shared ->
                Observable.merge(handle(shared))
            }
        }
    }

    private val reducer by lazy {
        BiFunction { previousState: S, result: R ->
            return@BiFunction reduce(result, previousState)
        }
    }

    private fun compose(): Observable<S> {
        return intentsSubject
            .compose(actionProcessor)
            .emitPartialState()
            // Cache each state and pass it to the reducer to create a new state from
            // the previous cached one and the latest Result emitted from the action processor.
            // The Scan operator is used here for the caching.
            .scan(initialState, reducer)
            // When a reducer just emits previousState, there's no reason to call render. In fact,
            // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
            // by showing the same snackbar twice in rapid succession).
            .distinctUntilChanged()
            // Emit the last one event of the stream on subscription
            // Useful when a View rebinds to the ViewModel after rotation.
            .replay(1)
            // Create the stream on creation without waiting for anyone to subscribe
            // This allows the stream to stay alive even when the UI disconnects and
            // match the stream's lifecycle to the ViewModel's one.
            .autoConnect(0)
    }


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    private fun Observable<R>.emitPartialState(): Observable<R> {
        return this.map {
            partialStatPublisher.onNext(it)
            it
        }
    }

}


@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
    return filter { !clazz.isInstance(it) }
}