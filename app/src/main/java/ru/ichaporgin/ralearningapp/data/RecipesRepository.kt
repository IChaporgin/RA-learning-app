package ru.ichaporgin.ralearningapp.data

import ru.ichaporgin.ralearningapp.data.RecipeApiService
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val recipeApiService: RecipeApiService,
) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getRecipes(ids: String): List<Recipe> = withContext(ioDispatcher) {
        try {
            recipeApiService.getRecipes(ids)
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategories", e)
            emptyList()
        }
    }

    suspend fun getRecipe(id: Int): Recipe? {
        return try {
            withContext(ioDispatcher) {
                recipeApiService.getRecipe(id)
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getRecipeById", e)
            null
        }
    }

    suspend fun getCategory(id: Int): Category? {
        return try {
            withContext(ioDispatcher) {
                recipeApiService.getCategory(id)
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategory", e)
            null
        }
    }

    suspend fun getRecipesByCategory(id: Int): List<Recipe> {
        return try {
            withContext(ioDispatcher) {
                val result = recipeApiService.getRecipesByCategory(id)
                Log.i(
                    "RecipesRepository",
                    "API returned recipes size=${result.size} for category id=$id"
                )
                result
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getRecipesByCategory failed", e)
            emptyList()
        }
    }

    suspend fun getCategories(): List<Category> {
        return try {
            withContext(ioDispatcher) {
                recipeApiService.getCategories()
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getCategories", e)
            emptyList()
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoryDao.getAllCategory()
        }
    }

    suspend fun saveCategoriesToCache(freshCategories: List<Category>) {
        return withContext(ioDispatcher) {
            categoryDao.insertCategory(freshCategories)
        }
    }

    suspend fun getRecipesFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipeDao.getAllRecipes()
        }
    }

    suspend fun saveRecipesToCache(freshRecipes: List<Recipe>) {
        return withContext(ioDispatcher) {
            recipeDao.insertRecipes(freshRecipes)
        }
    }

    suspend fun getFavoriteFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipeDao.getFavoriteRecipes()
        }
    }

    suspend fun updateFavorite(isFavorite: Boolean, id: Int) {
        return withContext(ioDispatcher) {
            recipeDao.updateFavorite(isFavorite, id)
        }
    }

    suspend fun getRecipeFromCache(id: Int): Recipe? {
        return withContext(ioDispatcher) {
            recipeDao.getRecipe(id)
        }
    }

    suspend fun saveRecipeToCache(recipe: Recipe) {
        return withContext(ioDispatcher) {
            recipeDao.insertRecipe(recipe)
        }
    }
}