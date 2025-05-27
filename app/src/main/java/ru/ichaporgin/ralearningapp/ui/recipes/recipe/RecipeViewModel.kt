package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipeState(
    val recipe: Recipe,
    val portion: Int = Constants.MAX_PORTIONS,
    val isFavorite: Boolean = false,
)

class RecipeViewModel : ViewModel() {
}