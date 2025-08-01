package ru.ichaporgin.ralearningapp.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.ichaporgin.ralearningapp.data.AppDatabase
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.RecipesRepository

class AppContainer(
    context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    )
        .build()

    private val categoryDao = db.categoryDao()
    private val recipeDao = db.recipeDao()
    private val logging = HttpLoggingInterceptor()
        .apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .client(client)
        .build()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val apiService = retrofit.create(RecipeApiService::class.java)

    val repository = RecipesRepository(
        recipeDao = recipeDao,
        categoryDao = categoryDao,
        recipeApiService = apiService,
        ioDispatcher = ioDispatcher
    )
    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}