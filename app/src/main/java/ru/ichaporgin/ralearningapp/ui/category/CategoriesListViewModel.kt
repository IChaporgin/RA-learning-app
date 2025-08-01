package ru.ichaporgin.ralearningapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Category

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val isError: Boolean = false
)

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState
    fun loadData() {
        viewModelScope.launch {
            val cachedCategory = recipesRepository.getCategoriesFromCache()
            _categoriesState.postValue(
                CategoriesState(
                    categories = cachedCategory,
                    isError = cachedCategory.isEmpty()
                )
            )
            val freshCategories = recipesRepository.getCategories()
            if (freshCategories.isNotEmpty()) {
                recipesRepository.saveCategoriesToCache(freshCategories)
                _categoriesState.postValue(
                    CategoriesState(
                        categories = cachedCategory,
                        isError = false
                    )
                )
            } else {
                if (cachedCategory.isEmpty()) {
                    _categoriesState.postValue(
                        CategoriesState(
                            categories = emptyList(),
                            isError = true
                        )
                    )
                }
            }
        }
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        viewModelScope.launch {
            val cachedCategories = recipesRepository.getCategoriesFromCache()
            val cachedCategory = cachedCategories.find { it.id == categoryId }
            if (cachedCategory != null) {
                callback(cachedCategory)
            } else {
                val categoryFromApi = recipesRepository.getCategory(categoryId)
                if (categoryFromApi != null) {
                    recipesRepository.saveCategoriesToCache(listOf(categoryFromApi))
                }
                callback(categoryFromApi)
            }
        }
    }
}