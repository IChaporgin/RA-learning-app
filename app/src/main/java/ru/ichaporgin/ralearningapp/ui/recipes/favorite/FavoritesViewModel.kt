package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe
import javax.inject.Inject

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val selectedFavorites: LiveData<FavoritesState> get() = _favoritesState


    fun loadData() {
        viewModelScope.launch {
            val recipes = repository.getFavoriteFromCache()
            _favoritesState.value = _favoritesState.value?.copy(
                recipes = recipes,
            )
            Log.d("CategoriesListFragment", "Картинка успешно загружена")
        }
    }
}