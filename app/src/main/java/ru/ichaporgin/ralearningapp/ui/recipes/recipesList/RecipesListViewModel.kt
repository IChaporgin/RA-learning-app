package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ichaporgin.ralearningapp.data.RecipesRepository
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipesState(
    val recipes: List<Recipe> = emptyList(),
    val recipesListImageUrl: String? = null
)

class RecipesListViewModel(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> get() = _recipesState

    fun loadRecipes(id: Int) {
        viewModelScope.launch {
            val cachedRecipes = repository.getRecipesFromCache()
            val recipes = cachedRecipes.filter { it.categoryId == id }
            Log.i("RecipesVM", "Cached recipes before loading from network: ${cachedRecipes.size}")
            _recipesState.value = RecipesState(recipes)

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