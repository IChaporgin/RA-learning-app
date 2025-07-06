package ru.ichaporgin.ralearningapp.data

import RecipeApiService
import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe

class RecipesRepository {
    private val apiService: RecipeApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory("application/json".toMediaType())
            )
            .build()

        apiService = retrofit.create(RecipeApiService::class.java)
    }

    fun getRecipes(ids: Set<String?>): List<Recipe> {
        return try {
            val response = apiService.getRecipes(ids).execute()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RecipesRepository", "getCategories error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategories", e)
            emptyList()
        }
    }

    fun getRecipe(id: Int): Recipe? {
        return try {
            val response = apiService.getRecipe(id).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RecipesRepository", "getRecipeById error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getRecipeById", e)
            null
        }
    }

    fun getCategory(id: Int): Category? {
        return try {
            val response = apiService.getCategory(id).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RecipesRepository", "getCategory error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Exception in getCategory", e)
            null
        }
    }

    fun getRecipesByCategory(id: Int): List<Recipe> {
        return try {
            val response = apiService.getRecipesByCategory(id).execute()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RecipesRepository", "getRecipesByCategory error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getRecipesByCategory", e)
            emptyList()
        }
    }

    fun getCategories(): List<Category> {
        return try {
            val response = apiService.getCategories().execute()
            if (response.isSuccessful){
                response.body() ?: emptyList()
            } else {
                Log.e("RecipesRepository", "getCategories error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "getCategories", e)
            emptyList()
        }
    }
}