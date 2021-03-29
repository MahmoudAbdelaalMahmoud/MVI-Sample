package com.mahmoud.mvisample.data

import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.boundaries.mapper.DtoToModelMapper
import com.mahmoud.mvisample.data.remote.RecipeDto
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: IDataSource,
    private val mapper: DtoToModelMapper,
) : IRepository {
    override suspend fun getRecipeList(page: Int): RecipeListPartialState {
//       return try {
           return  with(dataSource.getRecipeList(page)) {
                if (page == FIRST_PAGE) firstPageResult()
                else otherPageResult()
            }
//        }catch (e:Exception){
//            getErrorAccordingPage(page == FIRST_PAGE,e)
//        }
    }


    private fun List<RecipeDto>.firstPageResult(): RecipeListPartialState {
        return if (isEmpty()) RecipeListPartialState.Empty
        else RecipeListPartialState.ListResult.ListResultInitial(this.map { mapper.fromDtoToModel(it) },
            isLast = false)
    }

    private fun List<RecipeDto>.otherPageResult(): RecipeListPartialState {
        return if (isEmpty()) RecipeListPartialState.ListResult.ListResultLoadMore(this.map { mapper.fromDtoToModel(it) },
            isLast = true)
        else RecipeListPartialState.ListResult.ListResultLoadMore(this.map { mapper.fromDtoToModel(it) },
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