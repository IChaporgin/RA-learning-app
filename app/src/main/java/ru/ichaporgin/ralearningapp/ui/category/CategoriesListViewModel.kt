package ru.ichaporgin.ralearningapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val handler = Handler(Looper.getMainLooper())
    private val threadPool = AppThread.threadPool
    fun loadData() {
        threadPool.execute {
            val categories = repository.getCategories()
            val isEmpty = categories.isEmpty()
            val context = getApplication<Application>().applicationContext
            val drawable = try {
                val inputStream = context.assets.open("categories.png")
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Ошибка загрузки картинки", e)
                null
            }
            if (isEmpty) {
                Log.i("!!!", "Categories isEmpty!")

            }
            _categoriesState.postValue(
                CategoriesState(
                    categories = categories,
                    categoriesImage = drawable,
                    isError = isEmpty
                )
            )
        viewModelScope.launch {
            val categories = withContext(Dispatchers.IO) { repository.getCategories() }
            val assetPath = "categories.png"
            if (categories.isEmpty()) {
                Log.i("!!!", "Ошибка загрузки данных категорий.")
            }
            _categoriesState.value = CategoriesState(
                categories = categories,
                assetPath
            )
        }
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        viewModelScope.launch {
            val category = withContext(Dispatchers.IO) { repository.getCategory(categoryId) }
            handler.post {
                callback(category)
            }
        }
    }
}