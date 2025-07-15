package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val imageFavoriteUrl: Drawable? = null,
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val selectedFavorites: LiveData<FavoritesState> get() = _favoritesState
    private val repository = RecipesRepository()


    fun loadData() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val pref =
                context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val favoriteIds: Set<String?> =
                pref.getStringSet(Constants.FAVORITES_KEY, emptySet()) ?: emptySet()
            val idsParam = favoriteIds.filterNotNull().joinToString(",")
            val recipes = repository.getRecipes(idsParam)
            val drawable = try {
                val inputStream = context.assets.open("bcg_favorites.png")
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Ошибка загрузки картинки", e)
                null
            }
            _favoritesState.value = _favoritesState.value?.copy(
                recipes = recipes,
                imageFavoriteUrl = drawable
            )
            Log.d("CategoriesListFragment", "Картинка успешно загружена")
        }
    }
}