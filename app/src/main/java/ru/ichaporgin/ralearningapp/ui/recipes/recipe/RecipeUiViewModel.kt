package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.model.Ingredient

data class RecipeUiState(
    var recipeId: Int? = null,
    var recipeTitle: String? = null,
    var recipeImage: String? = null,
    var ingredient: List<Ingredient> = listOf(),
    var method: List<String> = listOf(),
    var portion: Int = Constants.MAX_PORTIONS,
    var isFavorite: Boolean = false,
)

class RecipeUiViewModel : ViewModel() {
}