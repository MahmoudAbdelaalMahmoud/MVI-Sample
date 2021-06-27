package com.mahmoud.mvisample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.boundaries.mapper.DtoToModelMapper
import com.mahmoud.mvisample.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: IDataSource,
    private val mapper: DtoToModelMapper,
) : IRepository {
    override suspend fun getRecipeList(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecipePagingSource(dataSource,mapper, "") }
        ).flow
    }
}