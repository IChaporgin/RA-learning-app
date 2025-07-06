package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.AppThread
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val portionCount: Int = Constants.MIN_PORTIONS,
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val selectedRecipe: LiveData<RecipeState> get() = _recipeState
    private val repository = RecipesRepository()
    private val threadPoll = AppThread.threadPool

    fun loadRecipe(id: Int) {
        threadPoll.execute {
            Log.e("!!!", "load from network")
            val currentState = _recipeState.value ?: RecipeState()
            val portion = currentState.portionCount
            val recipe = repository.getRecipe(id)
            val favorites = getFavorites()
            val isFavorite = favorites.contains(id.toString())

            val recipeImage =
                try {
                    val assetManager = getApplication<Application>().assets
                    val inputStream = assetManager.open(recipe?.imageUrl ?: "none.jpg")
                    Drawable.createFromStream(inputStream, null)

                } catch (e: Exception) {
                    Log.e("RecipesListFragment", "Ошибка загрузки картинки", e)
                    null
                }
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                if (recipe == null) {
                    android.widget.Toast.makeText(
                        getApplication(),
                        "Ошибка получения рецепта",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }

                _recipeState.value = _recipeState.value?.copy(
                    recipe = recipe,
                    portionCount = portion,
                    isFavorite = isFavorite,
                    recipeImage = recipeImage,
                )
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(
            getApplication<Application>()
                .getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(Constants.FAVORITES_KEY, emptySet()) ?: emptySet()
        )
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value ?: return
        val recipeId = currentState.recipe?.id ?: return
        val favorites = getFavorites()
        val idString = recipeId.toString()

        val newIsFavorite = if (favorites.contains(idString)) {
            favorites.remove(idString)
            false
        } else {
            favorites.add(idString)
            true
        }
        saveFavorites(favorites)
        _recipeState.value = currentState.copy(isFavorite = newIsFavorite)
    }

    private fun saveFavorites(ids: Set<String>) {
        val pref = getApplication<Application>().getSharedPreferences(
            Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE
        )
        pref.edit {
            putStringSet(Constants.FAVORITES_KEY, ids)
        }
        Log.d("RecipeViewModel", "Текущие избранные: ${getFavorites()}")
    }

    fun updatePortion(portion: Int) {
        val currentState = _recipeState.value ?: return
        _recipeState.value = currentState.copy(portionCount = portion)
    }
}