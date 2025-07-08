package ru.ichaporgin.ralearningapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.AppThread
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Category

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val categoriesImage: Drawable? = null
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
            val drawable =
                try {
                    val assetManager = getApplication<Application>().assets
                    val inputStream = assetManager.open("categories.png")
                    Drawable.createFromStream(inputStream, null)
                } catch (e: Exception) {
                    Log.e("CategoriesViewModel", "Ошибка загрузки картинки", e)
                    null
                }
            handler.post {
                if (categories.isEmpty()) {
                    Toast.makeText(
                        getApplication(),
                        "Ошибка получения данных",
                        Toast.LENGTH_SHORT
                    ).show()
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
        }
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        threadPool.execute {
            val category = repository.getCategory(categoryId)
            handler.post {
               if (category == null) {
                   Toast.makeText(
                       getApplication(),
                       "Ошибка получения категории",
                       Toast.LENGTH_SHORT
                   ).show()
               }
                callback(category)
            }
        }
    }
}