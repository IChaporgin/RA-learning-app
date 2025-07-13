package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.AppThread
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipesState(
    val recipes: List<Recipe> = emptyList(),
    val recipesListImageUrl: String? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> get() = _recipesState
    private val repository = RecipesRepository()
    private val threadPool = AppThread.threadPool

    fun loadRecipes(id: Int) {
        threadPool.execute {
            val recipes = repository.getRecipesByCategory(id)
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            handler.post {
                _recipesState.value = _recipesState.value?.copy(recipes = recipes)
            }
        }
    }
}