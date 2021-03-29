package com.mahmoud.mvisample.util

import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return runCatching { offer(element) }.getOrDefault(false)
}

@ExperimentalCoroutinesApi
@CheckResult
fun SwipeRefreshLayout.refreshes(): Flow<Unit> {
    return callbackFlow {
        setOnRefreshListener {
            isRefreshing =false
            safeOffer(Unit)  }
        awaitClose { setOnRefreshListener(null) }
    }
}

@ExperimentalCoroutinesApi
@CheckResult
fun TextView.retry(): Flow<Unit> {
    return callbackFlow {
        setOnClickListener { safeOffer(Unit) }
        awaitClose { setOnClickListener(null) }
    }
}