package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val portion: Int = Constants.MAX_PORTIONS,
    val isFavorite: Boolean = false,
)

class RecipeViewModel : ViewModel() {
    private val mutableSelectedRecipe = MutableLiveData<RecipeState>()
    val selectedRecipe: LiveData<RecipeState> get() = mutableSelectedRecipe

    init {
        mutableSelectedRecipe.value = RecipeState(isFavorite = true)
    }
}