package com.mahmoud.mvisample.boundaries.mapper

import com.mahmoud.mvisample.data.remote.RecipeDto
import com.mahmoud.mvisample.domain.model.Recipe
import javax.inject.Inject

class DtoToModelMapper @Inject constructor() {
    fun fromDtoToModel(recipe: RecipeDto) = Recipe(recipe.pk,recipe.featuredImage, recipe.title, "${recipe.rating}")
}