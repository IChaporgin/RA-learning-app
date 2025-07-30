package ru.ichaporgin.ralearningapp.di

import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}