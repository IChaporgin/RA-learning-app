package ru.ichaporgin.ralearningapp.data

import RecipeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
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

    fun getRecipes(): Call<List<Recipe>> {
        return apiService.getRecipes()
    }

    fun getRecipe(id: Int): Call<Recipe> {
        return apiService.getRecipe(id)
    }

    fun getCategory(id: Int): Call<Category> {
        return apiService.getCategory(id)
    }

    fun getRecipesByCategory(id: Int): Call<List<Recipe>> {
        return apiService.getRecipesByCategory(id)
    }

    fun getCategories(): Call<List<Category>> {
        return apiService.getCategories()
    }
}