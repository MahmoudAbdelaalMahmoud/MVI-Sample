package com.mahmoud.mvisample.data.remote

import com.mahmoud.mvisample.data.IDataSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: IApiService) : IDataSource {
    override fun getRecipeBy(id: Int): Single<RecipeDto> = service.get(id)


    override fun getRecipeList(page: Int): Single<List<RecipeDto>> {
        return service.search(page).map { it.results }
    }
}