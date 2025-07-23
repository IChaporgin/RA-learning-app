package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.app.Application
import android.util.Log
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
            val cachedRecipes = repository.getRecipesFromCache(id)
            Log.i("RecipesVM", "Cached recipes before loading from network: ${cachedRecipes.size}")
            _recipesState.value = RecipesState(cachedRecipes)

            val freshRecipesApi = repository.getRecipesByCategory(id)
            val freshRecipes = freshRecipesApi.map {it.copy(categoryId = id)}
            Log.i("RecipesVM", "freshRecipes from network size = ${freshRecipes.size}")
            freshRecipes.forEach {
                Log.i("RecipesVM", "Recipe from network: id=${it.id}, title=${it.title}")
            }
            if (freshRecipes.isNotEmpty()) {
                repository.saveRecipesToCache(freshRecipes)
                Log.i("RecipesVM", "Cached recipes be after saving: ${freshRecipes.size}")
                _recipesState.value = _recipesState.value?.copy(recipes = freshRecipes)
            } else {
                if (cachedRecipes.isEmpty()) {
                    _recipesState.postValue(
                        RecipesState(
                            recipes = emptyList(),
                            recipesListImageUrl = null
                        )
                    )
                }
            }
        }
    }
}