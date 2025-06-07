package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val portion: Int = Constants.MAX_PORTIONS,
    val isFavorite: Boolean = false,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val selectedRecipe: LiveData<RecipeState> get() = _recipeState

    init {
        _recipeState.value = RecipeState(isFavorite = true)
//        loadRecipe(recipeId)
    }

    fun loadRecipe(id: Int) {
//        TODO("load from network")
        Log.e("!!!", "load from network")
        val recipe = STUB.getRecipeById(id)
        val portion = Constants.MIN_PORTIONS
//        val favorites = getFavorites()
//        val isFavorite = favorites.contains(id.toString())

        _recipeState.value = _recipeState.value?.copy(
            recipe = recipe,
            portion = portion,
        )
    }

    private fun getFavorites(): MutableSet<String> {
        val pref =
            getApplication<Application>().getSharedPreferences(
                Constants.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        val favoriteSet = pref.getStringSet(Constants.FAVORITES_KEY, emptySet())
        return HashSet(favoriteSet)
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
    }
}