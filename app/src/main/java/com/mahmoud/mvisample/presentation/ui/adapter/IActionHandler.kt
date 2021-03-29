package com.mahmoud.mvisample.presentation.ui.adapter

import com.mahmoud.mvisample.domain.model.Recipe

interface IActionHandler {
    fun openRecipe(item: Recipe)
    fun retry()
}