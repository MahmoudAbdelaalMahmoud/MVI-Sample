package com.mahmoud.mvisample.util

import timber.log.Timber

/**
 * A group  of inline function  for Timber
 *
 * using inline functions to set the calling class name as a tag
 */
@Suppress("NOTHING_TO_INLINE")
object Logger {

    /**
     * Log info message
     * @param msg of type [String]  info message to log
     */
    inline fun i(msg: String) {
        Timber.i(msg)
    }

    /**
     * log debug message
     * @param msg of type [String] debug message to log
     */
    inline fun d(msg: String) {
        Timber.d(msg)
    }

    /**
     * Log error message
     * @param msg of type [String] error message to log
     */
    inline fun e(msg: String) {
        Timber.e(msg)
    }

    /**
     * Log error [Throwable]
     * @param throwable of type [Throwable] error to log
     */
    inline fun e(throwable: Throwable) {
        Timber.e(throwable)
    }

    /**
     * Log error message and [Throwable]
     * @param msg of type [String] error message to log
     * @param throwable of type [Throwable] error to log
     */
    inline fun e(msg: String, throwable: Throwable) {
        Timber.e(throwable, msg)
    }

    /**
     * Log warning message
     * @param msg of type [String] warning message to log
     */
    inline fun w(msg: String) {
        Timber.w(msg)
    }

}