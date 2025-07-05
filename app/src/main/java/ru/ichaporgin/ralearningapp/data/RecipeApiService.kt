package ru.ichaporgin.ralearningapp.data

interface RecipeApiService {
    val baseUrl: String
    val recipeById: String
    val recipes: String
    val categoryById: String
    val recipesByCategoryId: String
    val category: String
}