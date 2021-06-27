package com.mahmoud.mvisample.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mahmoud.mvisample.boundaries.mapper.DtoToModelMapper
import com.mahmoud.mvisample.domain.model.Recipe

class RecipePagingSource(
    private val dataSource: IDataSource,
    private val mapper: DtoToModelMapper,
    private val query: String,
) :
    PagingSource<Int, Recipe>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val position = params.key ?: 1
        return try {
            val recipes = dataSource.getRecipeList(page = position, query = query)
                .map { mapper.fromDtoToModel(it) }
            val nextKey = if (recipes.isEmpty()){
                null
            }else{
                position + 1
            }
            LoadResult.Page(
                data = recipes,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}