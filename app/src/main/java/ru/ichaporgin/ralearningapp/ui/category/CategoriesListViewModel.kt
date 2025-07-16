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
    private val repository = RecipesRepository()
    fun loadData() {
        viewModelScope.launch {
            val categories = repository.getCategories()
            val drawable = try {
                val inputStream = getApplication<Application>().assets.open("categories.png")
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Ошибка загрузки картинки", e)
                null
            }
            if (categories.isEmpty()) {
                Log.i("!!!", "Categories isEmpty!")

            }
            _categoriesState.postValue(
                CategoriesState(
                    categories = categories,
                    categoriesImage = drawable,
                    isError = categories.isEmpty()
                )
            )
        }
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        viewModelScope.launch {
            val category = repository.getCategory(categoryId)
            callback(category)
        }
    }
}