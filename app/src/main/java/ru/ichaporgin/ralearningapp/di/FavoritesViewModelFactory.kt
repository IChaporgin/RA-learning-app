package ru.ichaporgin.ralearningapp.di

import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.ui.recipes.favorite.FavoritesViewModel

class FavoritesViewModelFactory(
    private val favoritesRepository: RecipesRepository
) : Factory<FavoritesViewModel>{
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(favoritesRepository)
    }
}