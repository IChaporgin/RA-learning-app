package ru.ichaporgin.ralearningapp.data

import RecipeApiService
import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe

class RecipesRepository(context: Context) {
    private val apiService: RecipeApiService
    private val json = Json { ignoreUnknownKeys = true }
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val categoryDao = db.categoryDao()
    private val recipeDao = db.recipeDao()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
        apiService = retrofit.create(RecipeApiService::class.java)
    }

    suspend fun getRecipes(ids: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            apiService.getRecipes(ids)
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategories", e)
            emptyList()
        }
    }

    suspend fun getRecipe(id: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            apiService.getRecipe(id)
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getRecipeById", e)
            null
        }
    }

    suspend fun getCategory(id: Int): Category? = withContext(Dispatchers.IO) {
        try {
            apiService.getCategory(id)
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategory", e)
            null
        }
    }

    suspend fun getRecipesByCategory(id: Int): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val result = apiService.getRecipesByCategory(id)
            Log.i("RecipesRepository", "API returned recipes size=${result.size} for category id=$id")
            result
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getRecipesByCategory failed", e)
            emptyList()
        }
    }

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            apiService.getCategories()
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getCategories", e)
            emptyList()
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> = withContext(Dispatchers.IO) {
        categoryDao.getAllCategory()
    }

    suspend fun saveCategoriesToCache(freshCategories: List<Category>) = withContext(Dispatchers.IO){
        categoryDao.insertCategory(freshCategories)
    }

    suspend fun getRecipesFromCache(): List<Recipe> = withContext(Dispatchers.IO) {
        recipeDao.getAllRecipes()
    }

    suspend fun saveRecipesToCache(freshRecipes: List<Recipe>) = withContext(Dispatchers.IO) {
        recipeDao.insertRecipes(freshRecipes)
    }

    suspend fun getFavoriteFromCache() = withContext(Dispatchers.IO) {
        recipeDao.getFavoriteRecipes()
    }

    suspend fun updateFavorite(isFavorite: Boolean, id: Int) = withContext(Dispatchers.IO) {
        recipeDao.updateFavorite(isFavorite, id)
    }

    suspend fun getRecipeFromCache(id: Int): Recipe? = withContext(Dispatchers.IO) {
        recipeDao.getRecipe(id)
    }

    suspend fun saveRecipeToCache(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDao.insertRecipe(recipe)
    }
}