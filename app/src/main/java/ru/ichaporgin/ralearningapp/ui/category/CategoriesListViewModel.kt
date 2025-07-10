package ru.ichaporgin.ralearningapp.ui.category

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.AppThread
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Category

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val categoriesImage: String? = null
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState
    private val repository = RecipesRepository()
    private val handler = Handler(Looper.getMainLooper())
    private val threadPool = AppThread.threadPool
    fun loadData() {
        threadPool.execute {
            threadPool.execute {
                val categories = repository.getCategories()
                val assetPath = "categories.png"
                handler.post {
                    if (categories.isEmpty()) {
                        Toast.makeText(
                            getApplication(),
                            "Ошибка получения данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    _categoriesState.value = CategoriesState(
                        categories = categories,
                        assetPath
                    )
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