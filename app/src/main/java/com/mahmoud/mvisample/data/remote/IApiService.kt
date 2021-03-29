package com.mahmoud.mvisample.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IApiService {
    @GET("search")
    suspend fun search(
        @Query("page") page: Int,
        @Query("query") query: String? = "",
    ): ResponseDto

    @GET("get")
    suspend fun get(@Query("id") id: Int): RecipeDto
}