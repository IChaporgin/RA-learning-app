package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.model.Recipe

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val imageFavorite: Drawable? = null,
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val selectedFavorites: LiveData<FavoritesState> get() = _favoritesState

    fun loadData() {
        val context = getApplication<Application>().applicationContext
        val pref = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val favoriteIds = pref.getStringSet(Constants.FAVORITES_KEY, emptySet()) ?: emptySet()
        val recipes = STUB.getRecipesByIds(favoriteIds)

        try {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open("bcg_favorites.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                _favoritesState.value = FavoritesState(
                    recipes = recipes,
                    imageFavorite = drawable
                )
                Log.d("CategoriesListFragment", "Картинка успешно загружена")
            } else {
                Log.e("CategoriesListFragment", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("CategoriesListFragment", "Ошибка загрузки картинки", e)
        }


    }
}