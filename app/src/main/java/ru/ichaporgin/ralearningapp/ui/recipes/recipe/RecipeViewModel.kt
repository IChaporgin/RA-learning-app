package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val portionCount: Int = Constants.MIN_PORTIONS,
    val isFavorite: Boolean = false,
    val recipeImageUrl: String? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val selectedRecipe: LiveData<RecipeState> get() = _recipeState
    private val repository = RecipesRepository(application)

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            val currentState = _recipeState.value ?: RecipeState()
            val portion = currentState.portionCount
            var recipe = repository.getRecipeFromCache(id)
            if (recipe == null) {
                recipe = repository.getRecipe(id)
                if (recipe != null) {
                    repository.saveRecipeToCache(recipe)
                }
            }
            val favorites = getFavorites()
            val isFavorite = favorites.contains(id.toString())
            val image = recipe?.let { Constants.IMG_URL + it.imageUrl }
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
                recipeImageUrl = image,
            )
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
        val currentState = _recipeState.value
        val recipeId = currentState?.recipe?.id
        if (recipeId != null) {
            viewModelScope.launch {
                val isFavorite = ids.contains(recipeId.toString())
                repository.updateFavorite(isFavorite, recipeId)
            }
        }
    }

    fun updatePortion(portion: Int) {
        val currentState = _recipeState.value ?: return
        _recipeState.value = currentState.copy(portionCount = portion)
    }
}