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

    fun loadCategories() {
        _categoriesState.value = CategoriesState(
            categories = STUB.getCategories()
        )
        try {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open("categories.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            _categoriesState.value = _categoriesState.value?.copy(categoriesImage = drawable)
            Log.d("CategoriesListFragment", "Картинка успешно загружена")
            Log.e("CategoriesListFragment", "Drawable == null")
        } catch (e: Exception) {
            Log.e("CategoriesListFragment", "Ошибка загрузки картинки", e)
        }
    }
}