package com.mahmoud.mvisample.domain.mvi

import androidx.paging.PagingData
import com.mahmoud.mvisample.domain.Constants.FIRST_PAGE
import com.mahmoud.mvisample.domain.model.Recipe

data class ViewState(
    val list: PagingData<Recipe>?= null
) : MVIViewState