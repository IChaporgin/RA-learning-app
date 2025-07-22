package ru.ichaporgin.ralearningapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Category

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val categoriesImage: Drawable? = null,
    val isError: Boolean = false
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState
    private val repository = RecipesRepository(context = application)
    fun loadData() {
        viewModelScope.launch {
            val drawable = try {
                val inputStream = getApplication<Application>().assets.open("categories.png")
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Ошибка загрузки картинки", e)
                null
            }

            val cachedCategory = repository.getCategoriesFromCache()
            _categoriesState.postValue(
                CategoriesState(
                    categories = cachedCategory,
                    categoriesImage = drawable,
                    isError = cachedCategory.isEmpty()
                )
            )
            val freshCategories = repository.getCategories()
            if (freshCategories.isNotEmpty()) {
                repository.saveCategoriesToCache(freshCategories)
                _categoriesState.postValue(
                    CategoriesState(
                        categories = cachedCategory,
                        categoriesImage = drawable,
                        isError = false
                    )
                )
            } else {
                if (cachedCategory.isEmpty()) {
                    _categoriesState.postValue(
                        CategoriesState(
                            categories = emptyList(),
                            categoriesImage = drawable,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        viewModelScope.launch {
            val cachedCategories = repository.getCategoriesFromCache()
            val cachedCategory = cachedCategories.find { it.id == categoryId }
            if (cachedCategory != null) {
                callback(cachedCategory)
            } else {
                val categoryFromApi = repository.getCategory(categoryId)
                if (categoryFromApi != null) {
                    repository.saveCategoriesToCache(listOf(categoryFromApi))
                }
                callback(categoryFromApi)
            }
        }
    }
}