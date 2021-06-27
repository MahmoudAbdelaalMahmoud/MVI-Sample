package com.mahmoud.mvisample.data.remote

import com.mahmoud.mvisample.data.IDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: IApiService) : IDataSource {
    override suspend fun getRecipeBy(id: Int): RecipeDto = service.get(id)
    override suspend fun getRecipeList(page: Int, query: String): List<RecipeDto> {
        return service.search(page,query).results ?: emptyList()
    }
}