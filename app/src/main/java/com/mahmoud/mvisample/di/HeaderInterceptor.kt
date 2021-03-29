package com.mahmoud.mvisample.di

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HeadersInterceptor @Inject constructor(): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(setupRequestHeaders(chain.request()))
    }
    private fun setupRequestHeaders(oldRequest: Request): Request {
        return oldRequest.newBuilder()
            .addHeader("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
            .build()
    }

}
