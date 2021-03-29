package com.mahmoud.mvisample.data.remote

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IApiService {
    @GET("search")
    fun search(
        @Query("page") page: Int,
        @Query("query") query: String? = "",
    ): Single<ResponseDto>

    @GET("get")
     fun get(@Query("id") id: Int): Single<RecipeDto>
}