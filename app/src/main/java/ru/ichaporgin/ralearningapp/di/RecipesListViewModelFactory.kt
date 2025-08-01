package ru.ichaporgin.ralearningapp.di

import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.ui.recipes.recipesList.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipesListViewModel>{
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}