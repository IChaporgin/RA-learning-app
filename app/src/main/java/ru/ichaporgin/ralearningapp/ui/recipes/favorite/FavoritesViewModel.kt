package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.model.Recipe

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val imageFavorite: Drawable? = null
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val selectedFavorites: LiveData<FavoritesState> get() = _favoritesState

    fun loadFavorites() {
        val context = getApplication<Application>().applicationContext
        val pref = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val favoriteIds = pref.getStringSet(Constants.FAVORITES_KEY, emptySet()) ?: emptySet()
        val recipes = STUB.getRecipesByIds(favoriteIds)
        _favoritesState.value = FavoritesState(recipes)
    }
}