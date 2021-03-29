package com.mahmoud.mvisample.di

import com.mahmoud.mvisample.util.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

/**
 * Logging Interceptor for logging the requests
 *
 */
class HttpLogger @Inject constructor(): HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Logger.d(message)
    }
}