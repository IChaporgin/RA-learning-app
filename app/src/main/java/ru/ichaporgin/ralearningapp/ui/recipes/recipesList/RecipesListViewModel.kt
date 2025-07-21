package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipesState(
    val recipes: List<Recipe> = emptyList(),
    val recipesListImageUrl: String? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> get() = _recipesState
    private val repository = RecipesRepository(application)

    fun loadRecipes(id: Int) {
        viewModelScope.launch {
            val recipes = repository.getRecipesFromCache(id)
//            val recipes = repository.getRecipesByCategory(id)
//            _recipesState.value = _recipesState.value?.copy(recipes = recipes)
        }
    }
}