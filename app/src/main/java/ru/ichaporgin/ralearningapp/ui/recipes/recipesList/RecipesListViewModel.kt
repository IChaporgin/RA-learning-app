package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.model.Recipe

data class RecipesState(
    val recipes: List<Recipe> = emptyList(),
    val image: Drawable? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> get() = _recipesState

    fun loadRecipes(id: Int) {
        _recipesState.value = _recipesState.value?.copy(
            recipes = STUB.getRecipesByCategoryId(id)
        )
    }

    fun loadImageFromAssets(fileName: String) {
        try {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open(fileName)
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                _recipesState.value = _recipesState.value?.copy(image = drawable)
                Log.d("RecipesListViewModel", "Картинка успешно загружена")
            } else {
                Log.e("RecipesListViewModel", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("RecipesListViewModel", "Ошибка загрузки картинки", e)
        }
    }
}