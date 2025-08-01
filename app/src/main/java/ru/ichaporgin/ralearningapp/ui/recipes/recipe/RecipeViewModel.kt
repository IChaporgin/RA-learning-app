package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe
import javax.inject.Inject

data class RecipeState(
    val recipe: Recipe? = null,
    val portionCount: Int = Constants.MIN_PORTIONS,
    val isFavorite: Boolean = false,
    val recipeImageUrl: String? = null,
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _recipeState = MutableLiveData(RecipeState())
    val selectedRecipe: LiveData<RecipeState> get() = _recipeState


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
            val favorites: List<Recipe> = repository.getFavoriteFromCache()
            val isFavorite = recipe?.id?.let { id -> favorites.any { it.id == id } } ?: false
            val image = recipe?.let { Constants.IMG_URL + it.imageUrl }
            _recipeState.value = _recipeState.value?.copy(
                recipe = recipe,
                portionCount = portion,
                isFavorite = isFavorite,
                recipeImageUrl = image,
            )
        }
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value ?: return
        val recipeId = currentState.recipe?.id ?: return
        viewModelScope.launch {
            val favorites = repository.getFavoriteFromCache()
            val isCurrentlyFavorites = favorites.any { it.id == recipeId }
            val newIsFavorite = !isCurrentlyFavorites
            repository.updateFavorite(newIsFavorite, recipeId)
            _recipeState.value = currentState.copy(isFavorite = newIsFavorite)
        }
    }

    fun updatePortion(portion: Int) {
        val currentState = _recipeState.value ?: return
        _recipeState.value = currentState.copy(portionCount = portion)
    }
}