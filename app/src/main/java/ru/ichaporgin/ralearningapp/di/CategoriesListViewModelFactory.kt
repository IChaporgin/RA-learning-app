package ru.ichaporgin.ralearningapp.di

import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.ui.category.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val categoriesRepository: RecipesRepository
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(categoriesRepository)
    }
}