package ru.ichaporgin.ralearningapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.model.Category

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val categoriesImage: Drawable? = null
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    fun loadData() {
        val categories = STUB.getCategories()
        val drawable =
            try {
                val assetManager = getApplication<Application>().assets
                val inputStream = assetManager.open("categories.png")
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Ошибка загрузки картинки", e)
                null
            }
        _categoriesState.value = _categoriesState.value?.copy(
            categories = categories,
            categoriesImage = drawable
        )
        if (drawable != null) {
            Log.d("CategoriesListFragment", "Картинка успешно загружена")
        } else {
            Log.e("CategoriesListFragment", "Drawable == null")
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return _categoriesState.value?.categories?.find { it.id == categoryId }
    }
}