package ru.ichaporgin.ralearningapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.ActivityMainBinding
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not to be null")

    private var category: MutableList<Category> = mutableListOf()

    private fun fetchRecipesFromCategory(categoryId: Int) {
        try {
            val client: OkHttpClient = OkHttpClient()
            val request: Request = Request.Builder()
                .url("${Constants.BASE_URL}category/$categoryId/recipes")
                .build()
            client.newCall(request).execute().use { response ->
                val json = response.body?.string()
                val type = object : TypeToken<List<Recipe>>() {}.type
                val recipes: List<Recipe> = Gson().fromJson(json, type)
                Log.i("!!!", "JSON Recipe: $recipes")
                Log.i("!!!", "Поток Recipe: ${Thread.currentThread()}")

            }
        } catch (e: Exception) {
            Log.e("!!!", "Ошибка соединения fetchRecipes:", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

        val thread = Thread {
            try {
                val client: OkHttpClient = OkHttpClient()
                val request: Request = Request.Builder()
                    .url("${Constants.BASE_URL}category")
                    .build()

                client.newCall(request).execute().use { response ->

                    val json = response.body?.string()
                    val type = object : TypeToken<List<Category>>() {}.type
                    category = Gson().fromJson(json, type)
                    Log.i("!!!", "JSON Category: $category")
                    val categoriesId = category.map { it.id }
                    categoriesId.forEach { categoryId ->
                        threadPool.execute {
                            fetchRecipesFromCategory(categoryId)
                        }
                    }

                    Log.i("!!!", "ResponseCode: ${response.code}")
                    Log.i("!!!", "ResponseMessage: ${response.message}")
                    Log.i("!!!", "ResponseBody: $json}")
                    Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка соединения:", e)
            }
        }

        thread.start()
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }


        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        @Suppress("DEPRECATION")
        window.navigationBarColor = ContextCompat.getColor(this, R.color.background_color)
        @Suppress("DEPRECATION")
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_color)
    }
}