package com.mahmoud.mvisample.data

import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.boundaries.mapper.DtoToModelMapper
import com.mahmoud.mvisample.data.remote.RecipeDto
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: IDataSource,
    private val mapper: DtoToModelMapper,
) : IRepository {
    override fun getRecipeList(page: Int): Single<RecipeListPartialState> {
        return dataSource.getRecipeList(page)
            .map {
                if (page == FIRST_PAGE) it.firstPageResult()
                else it.otherPageResult()
            }
    }


    private fun List<RecipeDto>.firstPageResult(): RecipeListPartialState {
        return if (isEmpty()) RecipeListPartialState.Empty
        else RecipeListPartialState.ListResult.ListResultInitial(this.map { mapper.fromDtoToModel(it) },
            isLast = false)
    }

    private fun List<RecipeDto>.otherPageResult(): RecipeListPartialState {
        return if (isEmpty()) RecipeListPartialState.ListResult.ListResultLoadMore(this.map {
            mapper.fromDtoToModel(it)
        },
            isLast = true)
        else RecipeListPartialState.ListResult.ListResultLoadMore(this.map {
            mapper.fromDtoToModel(it)
        },
            isLast = false)
    }

    private fun getErrorAccordingPage(
        isFirstPage: Boolean,
        throwable: Throwable,
    ): RecipeListPartialState.Error {
        return if (isFirstPage) RecipeListPartialState.Error.ErrorInitial(throwable)
        else RecipeListPartialState.Error.ErrorLoadMore(throwable)
    }
}